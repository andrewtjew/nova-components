package org.nova.frameworks;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.nova.annotations.Description;
import org.nova.concurrent.Future;
import org.nova.concurrent.TimerTask;
import org.nova.configuration.ConfigurationItem;
import org.nova.core.Utils;
import org.nova.html.pages.AjaxButton;
import org.nova.html.pages.AjaxQueryContentWriter;
import org.nova.html.pages.AjaxQueryResult;
import org.nova.html.pages.Attribute;
import org.nova.html.pages.HtmlWriter;
import org.nova.html.pages.Selection;
import org.nova.html.pages.TableList;
import org.nova.html.pages.operations.Menu;
import org.nova.html.pages.operations.OperationContentResult;
import org.nova.html.pages.operations.OperationContentWriter;
import org.nova.http.Cookie;
import org.nova.http.Header;
import org.nova.http.client.QueryBuilder;
import org.nova.http.client.TextClient;
import org.nova.http.client.TextResponse;
import org.nova.http.server.CSharpClassWriter;
import org.nova.http.server.ContentDecoder;
import org.nova.http.server.ContentEncoder;
import org.nova.http.server.ContentReader;
import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;
import org.nova.http.server.Filter;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.HttpServer;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.ParameterInfo;
import org.nova.http.server.ParameterSource;
import org.nova.http.server.RequestHandler;
import org.nova.http.server.RequestHandlerNotFoundLogEntry;
import org.nova.http.server.RequestLogEntry;
import org.nova.http.server.Response;
import org.nova.http.server.TextContentWriter;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.DefaultValue;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.POST;
import org.nova.http.server.annotations.Path;
import org.nova.http.server.annotations.PathParam;
import org.nova.http.server.annotations.QueryParam;
import org.nova.json.JSONString;
import org.nova.logging.JSONBufferedLZ4Queue;
import org.nova.logging.LogDirectoryInfo;
import org.nova.logging.LogDirectoryManager;
import org.nova.metrics.AverageAndRate;
import org.nova.metrics.CategoryMeters;
import org.nova.metrics.CountAverageRateMeter;
import org.nova.metrics.CountAverageRateMeterBox;
import org.nova.metrics.CountMeter;
import org.nova.metrics.CountMeterBox;
import org.nova.metrics.LevelMeter;
import org.nova.metrics.LevelMeterBox;
import org.nova.metrics.RateMeter;
import org.nova.metrics.RateMeterBox;
import org.nova.operations.OperatorVariable;
import org.nova.test.Testing;
import org.nova.testing.TestTraceClient;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceNode;
import org.nova.tracing.TraceStats;

import com.google.common.base.Strings;
import com.sun.jersey.api.client.ClientResponse.Status;

@Description("Handlers for server operator pages")
@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({ JSONContentReader.class, JSONPatchContentReader.class })
@ContentWriters({OperationContentWriter.class, TextContentWriter.class})
public class ServerOperatorPages
{
    public final ServerApplication serverApplication;

    @org.nova.operations.OperatorVariable(description = "Sampling duration (seconds)", minimum = "0.1")
    private double rateSamplingDuration = 10;

    @org.nova.operations.OperatorVariable(description = "cache max-age in (seconds)", minimum = "1")
    private int cacheMaxAge = 300;

    @OperatorVariable(description = "cache control value returned to client other than max-age (e.g.: no-transform, public)")
    private String cacheControlValue = "public";

    public ServerOperatorPages(ServerApplication serverApplication) throws Throwable
    {
        this.rateSamplingDuration = serverApplication.getConfiguration().getDoubleValue("ServerOperatorPages.meters.rateSamplingDuration", 10);
        this.cacheMaxAge = serverApplication.getConfiguration().getIntegerValue("ServerOperatorPages.cache.maxAge", 300);
        this.cacheControlValue = serverApplication.getConfiguration().getValue("ServerOperatorPages.cache.controlValue", "public");
        this.serverApplication = serverApplication;

        Menu menu = serverApplication.getPageContentWriter().getMenu();

        menu.add("Process|Configuration", "/operator/process/configuration");
        menu.add("Process|Futures", "/operator/process/futures");
        menu.add("Process|Timer Tasks", "/operator/process/timers");
        menu.add("Process|Meters|Categories", "/operator/process/meters/categories");
        menu.add("Process|Meters|Level Meters", "/operator/process/meters/levelMeters");
        menu.add("Process|Meters|Rate Meters", "/operator/process/meters/rateMeters");
        menu.add("Process|Meters|Count Meters", "/operator/process/meters/countMeters");
        menu.add("Process|Meters|CountAverageRate Meters", "/operator/process/meters/countAverageRateMeters");

        menu.add("Tracing|Active Trace Stats", "/operator/tracing/activeStats");
        menu.add("Tracing|Last Trace Stats", "/operator/tracing/lastStats");
        menu.add("Tracing|Trace Graph", "/operator/tracing/traceGraph");
        menu.add("Tracing|Root Categories", "/operator/tracing/traceRoots");
        menu.add("Tracing|All Categories", "/operator/tracing/allCategories");
        menu.add("Tracing|Active Traces", "/operator/tracing/activeTraces");
        menu.add("Tracing|Last Traces", "/operator/tracing/lastTraces");
        menu.add("Tracing|Last Exception Traces", "/operator/tracing/lastExceptions");

        menu.add("Logging|Status", "/operator/logging/status");

        menu.add("Servers|Public|Performance", "/operator/httpServer/performance/public");
        menu.add("Servers|Public|Status", "/operator/httpServer/status/public");
        menu.add("Servers|Public|Last Requests", "/operator/httpServer/lastRequests/public");
        menu.add("Servers|Public|Last Not Founds (404s)", "/operator/httpServer/lastNotFounds/public");
        menu.add("Servers|Public|Last Exception Requests", "/operator/httpServer/lastExceptionRequests/public");
        menu.add("Servers|Public|Methods", "/operator/httpServer/methods/public");
        menu.add("Servers|Public|Class Definitions", "/operator/httpServer/classDefinitions/public");

        menu.add("Servers|Private|Status", "/operator/httpServer/status/private");
        menu.add("Servers|Private|Performance", "/operator/httpServer/performance/private");
        menu.add("Servers|Private|Last Requests", "/operator/httpServer/lastRequests/private");
        menu.add("Servers|Private|Last Not Founds (404s)", "/operator/httpServer/lastNotFounds/private");
        menu.add("Servers|Private|Last Exception Requests", "/operator/httpServer/lastExceptionRequests/private");
        menu.add("Servers|Private|Methods", "/operator/httpServer/methods/private");
        menu.add("Servers|Private|Class Definitions", "/operator/httpServer/classDefinitions/private");
        
        menu.add("Servers|Operator|Status", "/operator/httpServer/status/operator");
        menu.add("Servers|Operator|Performance", "/operator/httpServer/performance/operator");
        menu.add("Servers|Operator|Last Requests", "/operator/httpServer/lastRequests/operator");
        menu.add("Servers|Operator|Last Not Founds (404s)", "/operator/httpServer/lastNotFounds/operator");
        menu.add("Servers|Operator|Last Exception Requests", "/operator/httpServer/lastExceptionRequests/operator");
        menu.add("Servers|Operator|Methods", "/operator/httpServer/methods/operator");
        menu.add("Servers|Operator|Class Definitions", "/operator/httpServer/classDefinitions/operator");
        serverApplication.getOperatorVariableManager().register(serverApplication.getTraceManager());
        serverApplication.getOperatorVariableManager().register(this);
    }

    @GET
    @Path("/operator/process/configuration")
    public Response<OperationContentResult> configuration()
    {
        HtmlWriter writer = new HtmlWriter();
        writer.begin_sortableTable(1);
        writer.tr(writer.inner().th("Name").th("Value").th("Description").th("Source").th("Source Context"));
        for (ConfigurationItem item : this.serverApplication.getConfiguration().getConfigurationItemSnapshot())
        {
            writer.tr(writer.inner().td(item.getName()).td(item.getValue()).td(item.getDescription()).td(item.getSource()).td(item.getSourceContext()));
        }
        writer.end_table();
        return OperationContentResult.respond(writer, "Configuration");
    }

    @GET
    @Path("/operator/process/futures")
    public Response<OperationContentResult> futures()
    {
        HtmlWriter writer = new HtmlWriter();
        Future<?>[] array = this.serverApplication.getFutureScheduler().getFutureSnapshot();
        writer.p("Total:" + array.length);
        if (array.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Category").th("Number").th("Duration (ms)").th("Executing").th("Ended"));
            for (Future<?> item : array)
            {
                writer.tr(writer.inner().td(item.getTrace().getCategory()).td(item.getTrace().getNumber()).td(Utils.nanosToDurationString(item.getTrace().getDurationNs())).td(item.getExecuting())
                        .td(item.getCompleted()));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Futures");
    }

    void write(HtmlWriter writer, Trace trace, Object family)
    {
        writer.tr(writer.inner().td(family).td(writer.inner().a(trace.getCategory(), "./activeTrace/" + trace.getNumber())).td(trace.getNumber())
                .td(Utils.millisToLocalDateTime(trace.getCreated())).td(trace.getActiveNs() / 1000000).td(trace.getWaitNs() / 1000000).td(trace.getDurationNs() / 1000000)
                .td(trace.isWaiting()).td(trace.getDetails()).td(trace.getThread().getName()));

    }

    @GET
    @Path("/operator/tracing/activeTrace/{number}")
    public Response<OperationContentResult> activeTrace(@PathParam("number") long number)
    {
        HtmlWriter writer = new HtmlWriter();
        Trace[] traces = this.serverApplication.getTraceManager().getActiveSnapshot();
        Trace found = null;
        for (Trace trace : traces)
        {
            if (trace.getNumber() == number)
            {
                found = trace;
                break;
            }
        }
        if (found != null)
        {
            writer.begin_table(1);
            writer.tr(writer.inner().th("T", "*=target trace, A=ancestor, number=child number").th("Category").th("Number").th("Created").th("Active", "(ms)").th("Wait", "(ms)")
                    .th("Duration", "Active+Wait (ms)").th("Waiting").th("Details").th("thread"));

            ArrayList<ArrayList<Trace>> childeren = new ArrayList<>();
            for (Trace trace : traces)
            {
                ArrayList<Trace> list = new ArrayList<>();
                for (Trace parent = trace; parent != null; parent = parent.getParent())
                {
                    if (parent.getNumber() == number)
                    {
                        childeren.add(list);
                        break;
                    }
                    list.add(parent);
                }
            }

            int index = 0;
            for (ArrayList<Trace> list : childeren)
            {
                for (Trace trace : list)
                {
                    write(writer, trace, index);
                }
                index++;
            }
            Trace trace = found;
            write(writer, trace, "*");
            for (trace = found.getParent(); trace != null; trace = trace.getParent())
            {
                write(writer, trace, "A");
            }
            writer.end_table();
            writer.br();
            writer.p("Stack");
            StackTraceElement[] stackTrace = found.getThread().getStackTrace();
            writer.textarea(Utils.toString(stackTrace), true, stackTrace.length + 1, 160);

        }
        else
        {
            writer.text("trace ended");
        }
        return OperationContentResult.respond(writer, "Active Trace");
    }

    @GET
    @Path("/operator/tracing/activeStats")
    public Response<OperationContentResult> activeTraces(@QueryParam("hideWaiting") @DefaultValue("false") boolean hideWaiting)
    {
        HtmlWriter writer = new HtmlWriter();
        Trace[] traces = this.serverApplication.getTraceManager().getActiveSnapshot();
        writer.text("Total:" + traces.length);
        if (hideWaiting == false)
        {
            writer.text(" ");
            writer.a("Hide Waiting", "?hideWaiting=true");
        }
        else
        {
            writer.text(" ");
            writer.a("Show Waiting", "?hideWaiting=false");

        }
        if (traces.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Category").th("Number").th("Active", "(ms)").th("Wait", "(ms)").th("Duration", "(ms)").th("Waiting").th("Details").th("Created"));
            for (Trace trace : traces)
            {
                if ((trace.isWaiting()) && (hideWaiting))
                {
                    continue;
                }
                writer.tr(writer.inner().td(writer.inner().a(trace.getCategory(), "./activeTrace/" + trace.getNumber())).td(trace.getNumber()).td(trace.getActiveNs() / 1000000)
                        .td(trace.getWaitNs() / 1000000).td(trace.getDurationNs() / 1000000).td(trace.isWaiting()).td(trace.getDetails())
                        .td(Utils.millisToLocalDateTime(trace.getCreated())));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Active Trace Stats");
    }

    @GET
    @Path("/operator/tracing/lastStats")
    public Response<OperationContentResult> lastStats() throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        TraceStats[] array = this.serverApplication.getTraceManager().getStatsSnapshotAndReset();
        writer.text("Total:" + array.length);
        if (array.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Category").th("Count").th("Average", "milliseconds").th("Stddev", "Standard deviation in milliseconds").th("Total", "Total duration in milliseconds"));
            for (TraceStats item : array)
            {
                CountAverageRateMeter meter = item.getMeter();
                AverageAndRate ar = meter.getMarkCountAverage(this.rateSamplingDuration);
                writer.tr(writer.inner().td(writer.inner().a(item.getCategory(), new QueryBuilder("./trace").add("category", item.getCategory()).toString())).td(meter.getCount())
                        .td(nanoToDefaultFormat(ar.getAverage())).td(nanoToDefaultFormat(ar.getStandardDeviation())).td(nanoToDefaultFormat(meter.getTotal())));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Last Trace Stats");
    }

    private static DecimalFormat millisecondFormat = new DecimalFormat("#.###");

    private String nanoToDefaultFormat(double value)
    {
        return millisecondFormat.format(value / 1.0e6);
    }

    private void writeTraceGraphNode(HtmlWriter writer, Entry<String, TraceNode> entry, int level) throws Exception
    {
        TraceNode node = entry.getValue();
        writer.begin_td();
        writer.a(entry.getKey(), new QueryBuilder("./trace").add("category", entry.getKey()).toString());
        writer.table(
                writer.inner().tr(writer.inner().th("Count").th("Average", "Milliseconds").th("Duration", "Milliseconds").th("Wait", "Milliseconds")).td(node.getCount())
                        .td(nanoToDefaultFormat(node.getTotalDurationNs() / node.getCount())).td(nanoToDefaultFormat(node.getTotalDurationNs())).td(nanoToDefaultFormat(node.getTotalWaitNs())),
                new Attribute("width", "100%"));

        writer.end_td();
        if (node.getChildTraces() != null)
        {
            writer.begin_td();
            writer.begin_table(1);
            for (Entry<String, TraceNode> child : node.getChildTraces().entrySet())
            {
                writer.begin_tr();
                writeTraceGraphNode(writer, child, level + 1);
                writer.end_tr();
            }
            writer.end_table();
            writer.end_td();
        }
    }

    private void writeTraceGraphNodeToCategory(HtmlWriter writer, String category,Entry<String, TraceNode> entry, int level) throws Exception
    {
        if (entry.getKey().equals(category))
        {
            writeTraceGraphNode(writer, entry, level + 1);
            return;
        }
        TraceNode node = entry.getValue();
        writer.begin_td();
        writer.a(entry.getKey(), new QueryBuilder("./trace").add("category", entry.getKey()).toString());
        writer.table(
                writer.inner().tr(writer.inner().th("Count").th("Average", "Milliseconds").th("Duration", "Milliseconds").th("Wait", "Milliseconds")).td(node.getCount())
                        .td(nanoToDefaultFormat(node.getTotalDurationNs() / node.getCount())).td(nanoToDefaultFormat(node.getTotalDurationNs())).td(nanoToDefaultFormat(node.getTotalWaitNs())),
                new Attribute("width", "100%"));

        writer.end_td();
        if (node.getChildTraces() != null)
        {
            writer.begin_td();
            writer.begin_table(1);
            for (Entry<String, TraceNode> child : node.getChildTraces().entrySet())
            {
                if (isChild(category, child))
                {
                    writer.begin_tr();
                    writeTraceGraphNodeToCategory(writer, category, child, level + 1);
                    writer.end_tr();
                }
            }
            writer.end_table();
            writer.end_td();
        }
    }

    @GET
    @Path("/operator/tracing/traceGraph")
    public Response<OperationContentResult> traceGraph() throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        Map<String, TraceNode> map = this.serverApplication.getTraceManager().getGraphSnapshot();
        writer.begin_table(1);
        for (Entry<String, TraceNode> entry : map.entrySet())
        {
            writer.begin_tr();
            writeTraceGraphNode(writer, entry, 0);
            writer.end_tr();
        }
        writer.end_table();
        return OperationContentResult.respond(writer, "Trace Graph");
    }

    @GET
    @Path("/operator/tracing/traceRoots")
    public Response<OperationContentResult> traceRoots() throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        Map<String, TraceNode> map = this.serverApplication.getTraceManager().getGraphSnapshot();
        writer.begin_sortableTable(1);
        writer.th("Category").th("Count").th("Average", "Milliseconds").th("Duration", "Milliseconds").th("Wait", "Milliseconds");

        for (Entry<String, TraceNode> entry : map.entrySet())
        {
            TraceNode node = entry.getValue();
            writer.tr().td(writer.inner().a(entry.getKey(), new QueryBuilder("./trace").add("category", entry.getKey()).toString()).td(node.getCount())
                    .td(nanoToDefaultFormat(node.getTotalDurationNs() / node.getCount())).td(nanoToDefaultFormat(node.getTotalDurationNs())).td(nanoToDefaultFormat(node.getTotalWaitNs())));

        }
        writer.end_table();
        return OperationContentResult.respond(writer, "Trace Roots");
    }

    boolean isChild(String category, Entry<String, TraceNode> entry)
    {
        if (entry.getKey().equals(category))
        {
            return true;
        }
        TraceNode node = entry.getValue();
        if (node.getChildTraces() == null)
        {
            return false;
        }
        for (Entry<String, TraceNode> child : node.getChildTraces().entrySet())
        {
            if (isChild(category, child) == true)
            {
                return true;
            }
        }
        return false;
    }

    @GET
    @Path("/operator/tracing/trace")
    public Response<OperationContentResult> trace(@QueryParam("category") String category) throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        writer.text("Subtree of category and path to root");
        Map<String, TraceNode> map = this.serverApplication.getTraceManager().getGraphSnapshot();
        writer.begin_table(1);
        for (Entry<String, TraceNode> entry : map.entrySet())
        {
            if (isChild(category, entry))
            {
                writer.begin_tr();
                writeTraceGraphNodeToCategory(writer, category, entry, 0);
                writer.end_tr();
            }
        }
        writer.end_table();

        return OperationContentResult.respond(writer, "Trace Category:" + category);
    }

    void addToAll(HashMap<String, ArrayList<TraceNode>> all, Entry<String, TraceNode> entry)
    {
        ArrayList<TraceNode> list = all.get(entry.getKey());
        if (list == null)
        {
            list = new ArrayList<>();
            all.put(entry.getKey(), list);
        }
        TraceNode node = entry.getValue();
        list.add(node);
        if (node.getChildTraces() != null)
        {
            for (Entry<String, TraceNode> child : node.getChildTraces().entrySet())
            {
                addToAll(all, child);
            }
        }
    }

    @GET
    @Path("/operator/tracing/allCategories")
    public Response<OperationContentResult> traceAllCategories() throws Exception
    {
        Map<String, TraceNode> map = this.serverApplication.getTraceManager().getGraphSnapshot();
        HashMap<String, ArrayList<TraceNode>> all = new HashMap<>();
        for (Entry<String, TraceNode> entry : map.entrySet())
        {
            addToAll(all, entry);
        }
        HtmlWriter writer = new HtmlWriter();
        writer.begin_sortableTable(1);
        writer.th("Category").th("Count").th("Total", "Total duration in seconds").th("Wait", "Total wait duration in seconds").th("occurs", "The number of times the category occurs in the trace graph").th("root");

        for (Entry<String, ArrayList<TraceNode>> entry : all.entrySet())
        {
            double totalDurationNs = 0;
            double totalWaitNs = 0;
            long count = 0;
            List<TraceNode> list = entry.getValue();
            for (TraceNode item : list)
            {
                count += item.getCount();
                totalDurationNs += item.getTotalDurationNs();
                totalWaitNs += item.getTotalWaitNs();
            }

            writer.tr().td(writer.inner().a(entry.getKey(), new QueryBuilder("./trace").add("category", entry.getKey()).toString()).td(count).td(nanoToDefaultFormat(totalDurationNs))
                    .td(nanoToDefaultFormat(totalWaitNs)).td(list.size()).td(map.containsKey(entry.getKey())));

        }
        writer.end_table();

        return OperationContentResult.respond(writer, "All Trace Categories");
    }

    private String toString(StackTraceElement[] elements, int start)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < elements.length; i++)
        {
            sb.append("at " + elements[i].toString() + "<br/>");
        }
        return sb.toString();
    }

    private void writeTraces(HtmlWriter writer, Trace[] traces)
    {
        writer.text("Count: " + traces.length);
        for (int i = traces.length - 1; i >= 0; i--)
        {
            Trace trace = traces[i];
            writer.h3(trace.getCategory());
            writer.begin_table(1);
            writer.th("Created").th("Number", "Sequence number").th("Parent", "Parent sequence number if trace has parent").th("Duration", "Milliseconds").th("Wait", "Milliseconds").th("Waiting")
                    .th("Closed").th("Thread").th("ThreadId");
            writer.tr(writer.inner().td(Utils.millisToLocalDateTimeString(trace.getCreated())).td(trace.getNumber()).td(trace.getParent() == null ? "" : trace.getParent().getNumber())
                    .td(nanoToDefaultFormat(trace.getActiveNs())).td(nanoToDefaultFormat(trace.getWaitNs())).td(trace.isWaiting()).td(trace.isClosed()).td(trace.getThread().getName())
                    .td(trace.getThread().getId()));
            writer.end_table();
            writer.begin_table(1);
            String details = trace.getDetails();
            if (details != null)
            {
                writer.tr(writer.inner().td("Details").td(details));
            }

            String fromLink = trace.getFromLink();
            if (fromLink != null)
            {
                writer.tr(writer.inner().td("FromLink").td(fromLink));
            }

            String toLink = trace.getToLink();
            if (toLink != null)
            {
                writer.tr(writer.inner().td("ToLink").td(toLink));
            }

            StackTraceElement[] currentStackTrace = trace.getThread().getStackTrace();
            if (currentStackTrace != null)
            {
                writer.tr(writer.inner().td("Current stack trace").td(toString(currentStackTrace, 0)));
            }

            StackTraceElement[] createStackTrace = trace.getCreateStackTrace();
            if (createStackTrace != null)
            {
                writer.tr(writer.inner().td("Create stack trace").td(toString(createStackTrace, 4)));
            }

            StackTraceElement[] closeStackTrace = trace.getCloseStackTrace();
            if (closeStackTrace != null)
            {
                writer.tr(writer.inner().td("Close stack trace").td(toString(closeStackTrace, 3)));
            }

            Throwable exception = trace.getThrowable();
            if (exception != null)
            {
                writer.tr(writer.inner().td("Exception").td(Utils.toString(exception)));
            }

            writer.end_table();
        }
    }

    @GET
    @Path("/operator/tracing/lastTraces")
    public Response<OperationContentResult> lastTraces() throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        writeTraces(writer, this.serverApplication.getTraceManager().getLastTraces());
        return OperationContentResult.respond(writer, "Last Traces");
    }

    @GET
    @Path("/operator/tracing/lastExceptions")
    public Response<OperationContentResult> lastExeptions() throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        writeTraces(writer, this.serverApplication.getTraceManager().getLastExceptionTraces());
        return OperationContentResult.respond(writer, "Last Exception Traces");
    }

    @GET
    @Path("/operator/tracing/activeTraces")
    public Response<OperationContentResult> activeTraces() throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        writeTraces(writer, this.serverApplication.getTraceManager().getActiveSnapshot());
        return OperationContentResult.respond(writer, "Active Traces");
    }

    @GET
    @Path("/operator/process/timers")
    public Response<OperationContentResult> timers()
    {
        HtmlWriter writer = new HtmlWriter();
        TimerTask[] timerTasks = this.serverApplication.getTimerScheduler().getTimerTaskSnapshot();
        writer.p("Total:" + timerTasks.length);
        if (timerTasks.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Category").th("N", "Number").th("Created").th("Status").th("Due").th("Countdown").th("Duration").th("SchedulingMode").th("Delay").th("Period")
                    .th("\u25BA", "Number of attempts").th("\u2714", "Number of successful executions").th("\u26d4", "Number of exceptions").th("\u2716", "Number of misses"));

            long now = System.currentTimeMillis();
            for (TimerTask timerTask : timerTasks)
            {
                writer.tr(writer.inner().td(timerTask.getCategory()).td(timerTask.getNumber()).td(Utils.millisToLocalDateTimeString(timerTask.getCreated())).td(timerTask.getExecutableStatus())
                        .td(Utils.millisToLocalDateTime(timerTask.getDue())).td(Utils.millisToDurationString(timerTask.getDue() - now))
                        .td(Utils.nanosToDurationString(timerTask.getTotalDuration())).td(timerTask.getShedulingMode()).td(timerTask.getDelay()).td(timerTask.getPeriod())
                        .td(timerTask.getAttempts()).td(timerTask.getSuccesses()).td(timerTask.getThrowables()).td(timerTask.getMisses()));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Timer Tasks");
    }

    @GET
    @Path("/operator/process/meters/levelMeters")
    public Response<OperationContentResult> levelMeters(@QueryParam("interval") @DefaultValue("10") long interval)
    {
        HtmlWriter writer = new HtmlWriter();
        LevelMeterBox[] boxes = this.serverApplication.getMeterManager().getSnapshot().getLevelMeterBoxes();
        writer.p("Total:" + boxes.length);
        if (boxes.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Category").th("Name").th("Level", "Highest").th("Highest TimeStamp").th("Description"));
            for (LevelMeterBox box : boxes)
            {
                LevelMeter meter = box.getMeter();
                writer.tr(writer.inner().td(box.getCategory()).td(box.getName()).td(meter.getLevel()).td(meter.getMaximumLevel())
                        .td(Utils.millisToLocalDateTimeString(meter.getHighestLevelTimeStamp())).td(box.getDescription()));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Level Meters");
    }

    @GET
    @Path("/operator/process/meters/rateMeters")
    public Response<OperationContentResult> countMeters(@QueryParam("interval") @DefaultValue("10") long interval)
    {
        HtmlWriter writer = new HtmlWriter();
        RateMeterBox[] boxes = this.serverApplication.getMeterManager().getSnapshot().getRateMeterBoxes();
        writer.p("Total:" + boxes.length);
        if (boxes.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Category").th("Name").th("Rate", "per second").th("Count").th("Average").th("Description"));
            for (RateMeterBox box : boxes)
            {
                RateMeter meter = box.getMeter();
                writer.tr(writer.inner().td(box.getCategory()).td(box.getName()).td(String.format("%.4f", meter.sampleRate(interval))).td(meter.getCount())
                        .td(String.format("%.4f", meter.sampleRate(interval))).td(box.getDescription()));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Rate Meters");
    }

    @GET
    @Path("/operator/process/meters/countMeters")
    public Response<OperationContentResult> countMeters()
    {
        HtmlWriter writer = new HtmlWriter();
        CountMeterBox[] boxes = this.serverApplication.getMeterManager().getSnapshot().getCountMeterBoxes();
        writer.p("Total:" + boxes.length);
        if (boxes.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Category").th("Name").th("Count").th("Description"));
            for (CountMeterBox box : boxes)
            {
                CountMeter meter = box.getMeter();
                writer.tr(writer.inner().td(box.getCategory()).td(box.getName()).td(meter.getCount()).td(box.getDescription()));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Count Meters");
    }

    @GET
    @Path("/operator/process/meters/countAverageRateMeters")
    public Response<OperationContentResult> countAverageRateMeters(@QueryParam("interval") @DefaultValue("10") long interval)
    {
        HtmlWriter writer = new HtmlWriter();
        CountAverageRateMeterBox[] boxes = this.serverApplication.getMeterManager().getSnapshot().getCountAverageRateMeterBoxes();
        writer.p("Total:" + boxes.length);
        if (boxes.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Category").th("Name").th("Average").th("StdDev", "Standard Deviation").th("Rate", "per second").th("Count").th("Total").th("Description"));
            for (CountAverageRateMeterBox box : boxes)
            {
                CountAverageRateMeter meter = box.getMeter();

                AverageAndRate averageAndRate = meter.getCountAverageRate(interval);
                writer.tr(writer.inner().td(box.getCategory()).td(box.getName()).td(String.format("%.4f", averageAndRate != null ? averageAndRate.getAverage() : "-"))
                        .td(String.format("%.4f", averageAndRate != null ? averageAndRate.getStandardDeviation() : "-"))
                        .td(String.format("%.4f", averageAndRate != null ? averageAndRate.getRate() : "-")).td(meter.getCount()).td(meter.getTotal()).td(box.getDescription()));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Count Average Rate Meters");
    }

    @GET
    @Path("/operator/process/meters/categories")
    public Response<OperationContentResult> meterCategories() throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        String[] categories = this.serverApplication.getMeterManager().getSnapshot().getCategories();
        writer.p("Total:" + categories.length);
        if (categories.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Categories"));
            for (String category : categories)
            {
                writer.tr(writer.inner().td(writer.inner().a(category, new QueryBuilder("/operator/process/meters/category").add("category", category).toString())));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Meter Categories");
    }

    @GET
    @Path("/operator/process/meters/category")
    public Response<OperationContentResult> meterCategory(@QueryParam("category") String category, @QueryParam("interval") @DefaultValue("10") long interval)
    {
        HtmlWriter writer = new HtmlWriter();

        CategoryMeters categories = this.serverApplication.getMeterManager().getSnapshot().getMeterBoxes(category);
        {
            LevelMeterBox[] boxes = categories.getLevelMeterBoxes();
            if ((boxes != null) && (boxes.length > 0))
            {
                writer.h2("Level Meters");
                writer.begin_sortableTable(1);
                writer.tr(writer.inner().th("Category").th("Name").th("Level", "Highest").th("Highest TimeStamp").th("Description"));
                for (LevelMeterBox box : boxes)
                {
                    LevelMeter meter = box.getMeter();
                    writer.tr(writer.inner().td(box.getCategory()).td(box.getName()).td(meter.getLevel()).td(meter.getMaximumLevel())
                            .td(Utils.millisToLocalDateTimeString(meter.getHighestLevelTimeStamp())).td(box.getDescription()));
                }
                writer.end_table();
            }
        }
        {
            RateMeterBox[] boxes = categories.getRateMeterBoxes();
            if ((boxes != null) && (boxes.length > 0))
            {
                writer.h2("Rate Meters");
                writer.begin_sortableTable(1);
                writer.tr(writer.inner().th("Name").th("Rate").th("Count").th("Average").th("Description"));
                for (RateMeterBox box : boxes)
                {
                    RateMeter meter = box.getMeter();
                    writer.tr(writer.inner().td(box.getName()).td(String.format("%.4f", meter.sampleRate(interval))).td(meter.getCount()).td(String.format("%.4f", meter.sampleRate(interval)))
                            .td(box.getDescription()));
                }
                writer.end_table();
            }
        }
        {
            CountMeterBox[] boxes = categories.getCountMeterBoxes();
            if ((boxes != null) && (boxes.length > 0))
            {
                writer.h2("Count Meters");
                writer.begin_sortableTable(1);
                writer.tr(writer.inner().th("Name").th("Count").th("Description"));
                for (CountMeterBox box : boxes)
                {
                    CountMeter meter = box.getMeter();
                    writer.tr(writer.inner().td(box.getName()).td(meter.getCount()).td(box.getDescription()));
                }
                writer.end_table();
            }
        }

        {
            CountAverageRateMeterBox[] boxes = categories.getCountAverageMeterBoxes();
            if ((boxes != null) && (boxes.length > 0))
            {
                writer.h2("Count Average Rate Meters");
                writer.begin_sortableTable(1);
                writer.tr(writer.inner().th("Name").th("Average").th("StdDev", "Standard Deviation").th("Rate", "per second").th("Count").th("Total").th("Description"));
                for (CountAverageRateMeterBox box : boxes)
                {
                    CountAverageRateMeter meter = box.getMeter();

                    AverageAndRate averageAndRate = meter.getCountAverageRate(interval);
                    writer.tr(writer.inner().td(box.getName()).td(String.format("%.4f", averageAndRate != null ? averageAndRate.getAverage() : "-"))
                            .td(String.format("%.4f", averageAndRate != null ? averageAndRate.getStandardDeviation() : "-"))
                            .td(String.format("%.4f", averageAndRate != null ? averageAndRate.getRate() : "-")).td(meter.getCount()).td(meter.getTotal()).td(box.getDescription()));
                }
                writer.end_table();
            }
        }

        return OperationContentResult.respond(writer, "Meter Category: " + category);
    }

    @GET
    @Path("/operator/httpServer/performance/{server}")
    public Response<OperationContentResult> performance(@PathParam("server")String server) throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        HttpServer httpServer=getHttpServer(server);
        RateMeter requestRateMeter = httpServer.getRequestRateMeter();
        double requestRate = requestRateMeter.sampleRate(this.rateSamplingDuration);
        writer.begin_table(0);
        writer.tableList("Request Rate", DOUBLE_FORMAT.format(requestRate));
        writer.tableList("Total Requests", requestRateMeter.getCount());
        writer.end_table();

        RequestHandler[] requestHandlers = httpServer.getRequestHandlers();
        writer.h2("Request Handlers");
        writer.p("Preferred Port: "+httpServer.getPreferredPort()+", Count:" + requestHandlers.length);
        if (requestHandlers.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Method").th("Count", "Count of total requests").th("% Count", "Count percentage")
                    .th("Duration", "Total duration in method in days hours:minutes:seconds.milliseconds").th("% Dur", "Percentage Duration")
                    .th("Ave Dur", "Average duration of request in milliseconds").th("Rate", "Request rate per second").th("ReqSize", "Average uncompressed size of request content in bytes")
                    .th("RespSize", "Average uncompressed size of response content in bytes"));
            double totalAll = 0;
            long totalCount = 0;
            for (RequestHandler requestHandler : requestHandlers)
            {
                Map<Integer, CountAverageRateMeter> meters = requestHandler.getStatusMeters();
                for (CountAverageRateMeter meter : meters.values())
                {
                    totalAll += meter.getTotal();
                    totalCount += meter.getCount();
                }
            }
            for (RequestHandler requestHandler : requestHandlers)
            {
                Map<Integer, CountAverageRateMeter> meters = requestHandler.getStatusMeters();
                long total = 0;
                long count = 0;
                double rate = 0;
                for (CountAverageRateMeter meter : meters.values())
                {
                    total += meter.getTotal();
                    count += meter.getCount();
                    AverageAndRate averageAndRate = meter.getCountAverageRate(this.rateSamplingDuration);
                    if (averageAndRate != null)
                    {
                        rate += averageAndRate.getRate();
                    }
                }
                double countPercentage = totalCount > 0 ? (100.0 * count) / totalCount : 0;
                double totalMilliseconds = total / 1.0e6;
                double totalPercentage = totalAll > 0 ? 100.0 * total / totalAll : 0;
                writer.tr(writer
                        .inner().td(
                                writer.inner()
                                        .a(requestHandler.getHttpMethod() + " " + requestHandler.getPath(),
                                                new QueryBuilder("/operator/httpServer/info").add("key", requestHandler.getKey()).toString())
                                        .td(count).td(DOUBLE_FORMAT.format(countPercentage)).td(Utils.millisToDurationString((long) totalMilliseconds)).td(DOUBLE_FORMAT.format(totalPercentage))
                                        .td(count > 0 ? DOUBLE_FORMAT.format(total / (1.0e6 * count)) : 0).td(DOUBLE_FORMAT.format(rate)))
                        .td(count > 0 ? requestHandler.getRequestUncompressedContentSizeMeter().getTotal() / count : 0)
                        .td(count > 0 ? requestHandler.getResponseUncompressedContentSizeMeter().getTotal() / count : 0));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Performance: "+server);
    }

    private void writeIfNotEmpty(HtmlWriter writer, String name, String value)
    {
        if ((value != null) && (value.length() > 0))
        {
            writer.tr(writer.inner().td(name).td(value));
        }
    }

    private void writeContent(HtmlWriter writer, String name, String value)
    {
        if ((value != null) && (value.length() > 0))
        {
            int rows = Utils.occurs(value, "\r") + 1;
            if (rows > 25)
            {
                rows = 25;
            }
            int cols = 160;
            writer.text(name);
            writer.textarea(value, new Attribute("style", "width:98%"), new Attribute("cols", cols), new Attribute("rows", rows), new Attribute("readonly", true));
        }
    }

    private void writeNonNull(HtmlWriter writer, String name, Object value)
    {
        if (value != null)
        {
            writer.tr(writer.inner().td(name).td(value));
        }
    }

    private void writeRequestLogEntries(HtmlWriter writer, RequestLogEntry[] entries) throws Exception
    {
        for (RequestLogEntry entry : entries)
        {
            writer.h2(entry.getRequestHandler().getKey());
            Trace trace = entry.getTrace();
            writer.begin_table(1);
            writer.tr(writer.inner().th("Number", "Trace number").th("Created").th("Duration", "milliseconds").th("Wait", "Amount of time spent waiting in milliseconds").th("FromLink"));
            writer.tr(writer.inner().td(trace.getNumber()).td(Utils.millisToLocalDateTimeString(trace.getCreated())).td(DOUBLE_FORMAT.format(trace.getDuration() * 1000.0))
                    .td(DOUBLE_FORMAT.format(trace.getWait() * 1000.0)).td(trace.getFromLink()));
            writer.end_table();
            if (trace.getThrowable() != null)
            {
                writeContent(writer, "Exception", Utils.toString(trace.getThrowable()));
            }

            writer.h3("Request");

            writer.begin_table(1);
            writeIfNotEmpty(writer, "Request", entry.getRequest());
            writeIfNotEmpty(writer, "Remote", entry.getRemoteEndPoint());
            writeIfNotEmpty(writer, "Query String", entry.getQueryString());
            writer.end_table();
            writeContent(writer, "Headers", entry.getRequestHeaders());
            writeContent(writer, "Content", entry.getRequestContentText());

            writer.h3("Response");
            writer.begin_table(1);
            writeNonNull(writer, "Status Code", entry.getStatusCode());
            writer.end_table();
            writeContent(writer, "Headers", entry.getResponseHeaders());
            writeContent(writer, "Content", entry.getResponseContentText());
        }
    }

    @GET
    @Path("/operator/httpServer/lastRequests/{server}")
    public Response<OperationContentResult> lastRequests(@PathParam("server") String server) throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        HttpServer httpServer=this.getHttpServer(server);
        RequestLogEntry[] entries = httpServer.getLastRequestLogEntries();
        writer.p("Preferred Port: "+httpServer.getPreferredPort()+", Count:" + entries.length);
        writeRequestLogEntries(writer, entries);
        return OperationContentResult.respond(writer, "Last Requests: "+server);
    }

    @GET
    @Path("/operator/httpServer/lastExceptionRequests/{server}")
    public Response<OperationContentResult> lastExceptionRequests(@PathParam("server") String server) throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        HttpServer httpServer=this.getHttpServer(server);
        RequestLogEntry[] entries = httpServer.getLastExceptionRequestLogEntries();
        writer.p("Preferred Port: "+httpServer.getPreferredPort()+", Count:" + entries.length);
        writeRequestLogEntries(writer, entries);
        return OperationContentResult.respond(writer, "Last Exception Requests: "+server);
    }

    @GET
    @Path("/operator/httpServer/lastNotFounds/{server}")
    public Response<OperationContentResult> lastNotFoundRequests(@PathParam("server") String server) throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        HttpServer httpServer=this.getHttpServer(server);
        RequestHandlerNotFoundLogEntry[] entries = httpServer.getRequestHandlerNotFoundLogEntries();
        writer.p("Preferred Port: "+httpServer.getPreferredPort()+", Count:" + entries.length);
        for (RequestHandlerNotFoundLogEntry entry : entries)
        {
            Trace trace = entry.getTrace();
            String key = entry.getMethod() + " " + entry.getURI();
            writer.h2(key);
            writer.begin_table(1);
            writer.tr(writer.inner().th("Number", "Trace number").th("Created").th("Duration", "milliseconds").th("Wait", "Amount of time spent waiting in milliseconds").th("FromLink").th("Remote")
                    .th("QueryString"));
            writer.tr(writer.inner().td(trace.getNumber()).td(Utils.millisToLocalDateTimeString(trace.getCreated())).td(DOUBLE_FORMAT.format(trace.getDuration() * 1000.0))
                    .td(DOUBLE_FORMAT.format(trace.getWait() * 1000.0)).td(trace.getFromLink()).td(entry.getRemoteEndPoint()).td(entry.getQueryString()));
            writer.end_table();
            writeContent(writer, "Headers", entry.getRequestHeaders());
        }
        return OperationContentResult.respond(writer, "Last Not Founds: "+server);
    }

    @GET
    @Path("/operator/httpServer/status/{server}")
    public Response<OperationContentResult> status(@PathParam("server") String server) throws Exception
    {
        HttpServer httpServer=getHttpServer(server);
        RequestHandler[] requestHandlers = httpServer.getRequestHandlers();
        HtmlWriter writer = new HtmlWriter();
        writer.p("Preferred Port: "+httpServer.getPreferredPort()+", Total:" + requestHandlers.length);
        if (requestHandlers.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Method").th("200").th("300").th("400").th("500"));
            for (RequestHandler requestHandler : requestHandlers)
            {
                HashMap<Integer, Long> statusCodes = new HashMap<>();
                statusCodes.put(200, 0L);
                statusCodes.put(300, 0L);
                statusCodes.put(400, 0L);
                statusCodes.put(500, 0L);
                Map<Integer, CountAverageRateMeter> meters = requestHandler.getStatusMeters();
                for (Entry<Integer, CountAverageRateMeter> entry : meters.entrySet())
                {
                    int status = entry.getKey() / 100 * 100;
                    Long count = statusCodes.get(status);
                    if (count == null)
                    {
                        count = entry.getValue().getCount();
                    }
                    else
                    {
                        count += entry.getValue().getCount();
                    }
                    statusCodes.put(status, count);
                }
                writer.tr(
                        writer.inner()
                                .td(writer.inner()
                                        .a(requestHandler.getHttpMethod() + " " + requestHandler.getPath(),
                                                new QueryBuilder("/operator/httpServer/info").add("key", requestHandler.getKey()).toString())
                                        .td(statusCodes.get(200)).td(statusCodes.get(300)).td(statusCodes.get(400)).td(statusCodes.get(500))));
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Performance: "+server);
    }

    
    static class ParameterDescription
    {
        final String type;
        final String name;
        final String defaultValue;
        final String description;
        ParameterDescription(String name,String type,String defaultValue,String description)
        {
            this.type=type;
            this.name=name;
            this.defaultValue=defaultValue;
            this.description=description;
        }
        
    }

    static class ContentTypeDescription
    {
        final String mediaType;
        final String schema;
        ContentTypeDescription(String mediaType,String schema)
        {
            this.mediaType=mediaType;
            this.schema=schema;
        }
    }
    
    static class RequestHandlerParameterDescriptions
    {
        final String method;
        final String pathSchema;
        final ParameterDescription[] cookieParameters;
        final ParameterDescription[] pathParameters;
        final ParameterDescription[] queryParameters;
        final ParameterDescription[] headerParameters;
        final ContentTypeDescription[] requestContentTypes;
        final ContentTypeDescription[] responseContentTypes;
        
        RequestHandlerParameterDescriptions(String method,String pathSchema,ParameterDescription[] cookieParameters,
                ParameterDescription[] pathParameters,
                ParameterDescription[] queryParameters,
                ParameterDescription[] headerParameters,
                ContentTypeDescription[] requestContentTypes,
                ContentTypeDescription[] responseContentTypes)
        {
            this.method=method;
            this.pathSchema=pathSchema;
            this.cookieParameters=cookieParameters;
            this.pathParameters=pathParameters;
            this.queryParameters=queryParameters;
            this.headerParameters=headerParameters;
            this.requestContentTypes=requestContentTypes;
            this.responseContentTypes=responseContentTypes;
        }
    }
    
    private RequestHandlerParameterDescriptions extractParameterDescriptions(RequestHandler requestHandler) throws Throwable
    {
        Method method=requestHandler.getMethod();
        ArrayList<ParameterDescription> cookieParameters=new ArrayList<>();
        ArrayList<ParameterDescription> pathParameters=new ArrayList<>();
        ArrayList<ParameterDescription> queryParameters=new ArrayList<>();
        ArrayList<ParameterDescription> headerParameters=new ArrayList<>();
        ParameterInfo contentParameterInfo = null;

        for (ParameterInfo info:requestHandler.getParameterInfos())
        {
            Parameter parameter = method.getParameters()[info.getIndex()];
            switch (info.getSource())
            {
            case CONTEXT:
            case STATE:
            case TRACE:
                continue;
            case CONTENT:
                contentParameterInfo=info;
                break;
            case COOKIE:
                cookieParameters.add(new ParameterDescription(info.getName(),info.getType().getSimpleName(),info.getDefaultValue()!=null?info.getDefaultValue().toString():null,getDescription(parameter)));
                break;
            case HEADER:
                headerParameters.add(new ParameterDescription(info.getName(),info.getType().getSimpleName(),info.getDefaultValue()!=null?info.getDefaultValue().toString():null,getDescription(parameter)));
                break;
            case PATH:
                pathParameters.add(new ParameterDescription(info.getName(),info.getType().getSimpleName(),info.getDefaultValue()!=null?info.getDefaultValue().toString():null,getDescription(parameter)));
                break;
            case QUERY:
                queryParameters.add(new ParameterDescription(info.getName(),info.getType().getSimpleName(),info.getDefaultValue()!=null?info.getDefaultValue().toString():null,getDescription(parameter)));
                break;
            }
        }

        ArrayList<ContentTypeDescription> requestContentDescriptions=new ArrayList<>();
        if (contentParameterInfo != null)
        {
            Parameter contentParameter = method.getParameters()[contentParameterInfo.getIndex()];
            if (requestHandler.getContentReaders().size() > 0)
            {
                for (ContentReader<?> contentReader : requestHandler.getContentReaders().values())
                {
                    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                    contentReader.writeSchema(byteOutputStream, contentParameter.getType());
                    String schema = new String(byteOutputStream.toByteArray());
                    if (schema.length()==0)
                    {
                        schema=null;
                    }
                    else
                    {
                        schema=schema.replace("\r\n", "");
                    }
                    requestContentDescriptions.add(new ContentTypeDescription(contentReader.getMediaType(), schema));
                }
            }
        }

        ArrayList<ContentTypeDescription> responseContentDescriptions=new ArrayList<>();
        Class<?> returnType = method.getReturnType();
        Type innerReturnType = null;
        if (returnType != void.class)
        {
            if (returnType == Response.class)
            {
                ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
                innerReturnType = type.getActualTypeArguments()[0];
                String typeName = innerReturnType.getTypeName();
                if (typeName.equals("?")==false)
                {
                    returnType = (Class<?>) innerReturnType;
                    innerReturnType = null;
                }
            }
            if (requestHandler.getContentWriters().size() > 0)
            {
                HashMap<String, ContentWriterList> lists = new HashMap<>();
                for (Entry<String, ContentWriter<?>> entry : requestHandler.getContentWriters().entrySet())
                {
                    ContentWriterList types = lists.get(entry.getValue().getMediaType());
                    if (types == null)
                    {
                        types = new ContentWriterList(entry.getValue());
                        lists.put(entry.getValue().getMediaType(), types);
                    }
                    types.types.add(entry.getKey());
                }

                for (ContentWriterList list : lists.values())
                {
                    if (innerReturnType == null)
                    {
                        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                        list.contentWriter.writeSchema(byteOutputStream, returnType);
                        String schema = new String(byteOutputStream.toByteArray());
                        if (schema.length()==0)
                        {
                            schema=null;
                        }
                        else
                        {
                            schema=schema.replace("\r\n", "");
                        }
                        responseContentDescriptions.add(new ContentTypeDescription(list.contentWriter.getMediaType(), schema));
                    }
                }
            }
        }
        ParameterDescription[] cookieParameterArray=cookieParameters.size()>0?cookieParameters.toArray(new ParameterDescription[cookieParameters.size()]):null;
        ParameterDescription[] pathParameterArray=pathParameters.size()>0?pathParameters.toArray(new ParameterDescription[pathParameters.size()]):null;
        ParameterDescription[] queryParameterArray=queryParameters.size()>0?queryParameters.toArray(new ParameterDescription[queryParameters.size()]):null;
        ParameterDescription[] headerParameterArray=headerParameters.size()>0?headerParameters.toArray(new ParameterDescription[headerParameters.size()]):null;
        ContentTypeDescription[] requestContentDescriptionArray=requestContentDescriptions.size()>0?requestContentDescriptions.toArray(new ContentTypeDescription[requestContentDescriptions.size()]):null;
        ContentTypeDescription[] responseContentDescriptionArray=responseContentDescriptions.size()>0?responseContentDescriptions.toArray(new ContentTypeDescription[responseContentDescriptions.size()]):null;
        
        return new RequestHandlerParameterDescriptions(requestHandler.getHttpMethod(),requestHandler.getPath(),cookieParameterArray, pathParameterArray, queryParameterArray, headerParameterArray, requestContentDescriptionArray, responseContentDescriptionArray); 
    }
    
    @GET
    @ContentWriters(JSONContentWriter.class)
    @Path("/operator/httpServer/apis")
    public RequestHandlerParameterDescriptions[] apis() throws Throwable
    {
        RequestHandler[] requestHandlers = this.serverApplication.getOperatorServer().getRequestHandlers();
        ArrayList<RequestHandlerParameterDescriptions> list=new ArrayList<>();
        for (RequestHandler requestHandler : requestHandlers)
        {
            list.add(extractParameterDescriptions(requestHandler));
        }
        return list.toArray(new RequestHandlerParameterDescriptions[list.size()]);
    }

    @GET
    @ContentWriters(JSONContentWriter.class)
    @Path("/operator/httpServer/api")
    public RequestHandlerParameterDescriptions api(@QueryParam("key") String key) throws Throwable
    {
        RequestHandler requestHandler = this.serverApplication.getOperatorServer().getRequestHandler(key);
        return extractParameterDescriptions(requestHandler);
    }    

    
    private HttpServer getHttpServer(String server)
    {
        if ("public".equals(server))
        {
            return this.serverApplication.getPublicServer();
        }
        else if ("private".equals(server))
        {
            return this.serverApplication.getPrivateServer();
        }
        return this.serverApplication.getOperatorServer();
    }
    
    @GET
    @Path("/operator/httpServer/methods/{server}")
    public Response<OperationContentResult> methods(@PathParam("server") String server) throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        HttpServer httpServer=getHttpServer(server);
        RequestHandler[] requestHandlers = httpServer.getRequestHandlers();
        writer.p("Preferred Port: "+httpServer.getPreferredPort()+", Total:" + requestHandlers.length);
        if (requestHandlers.length > 0)
        {
            writer.begin_sortableTable(1);
            writer.tr(writer.inner().th("Method").th("Description").th("Filters"));
            for (RequestHandler requestHandler : requestHandlers)
            {
                writer.begin_tr();
                writer.td(writer.inner().a(requestHandler.getHttpMethod() + " " + requestHandler.getPath(),
                        new QueryBuilder("/operator/httpServer/method/"+server).add("key", requestHandler.getKey()).toString()));
                Method method = requestHandler.getMethod();
                Description description = method.getAnnotation(Description.class);
                if (description != null)
                {
                    writer.td(description.value());
                }
                else
                {
                    writer.td();
                }

                Filter[] filters = requestHandler.getFilters();
                if (filters != null)
                {
                    StringBuilder sb = new StringBuilder();
                    for (Filter filter : filters)
                    {
                        if (sb.length() > 0)
                        {
                            sb.append(',');
                        }
                        sb.append(filter.getClass().getSimpleName());
                    }
                    writer.td(sb.toString());
                }
                else
                {
                    writer.td();
                }

                writer.end_tr();
            }
            writer.end_table();
        }
        return OperationContentResult.respond(writer, "Methods: "+server);
    }

    private String getDescription(Method method)
    {
        Description description = method.getAnnotation(Description.class);
        if (description != null)
        {
            return description.value();
        }
        return null;
    }
    private String getDescription(Parameter parameter)
    {
        Description description = parameter.getAnnotation(Description.class);
        if (description != null)
        {
            return description.value();
        }
        return null;
    }

    private void writeParameterInfos(HtmlWriter writer, String heading, RequestHandler handler, ParameterSource filter)
    {
        if (Arrays.stream(handler.getParameterInfos()).filter(info ->
        {
            return info.getSource() == filter;
        }).count() == 0)
        {
            return;
        }

        writer.h4(heading);
        Method method = handler.getMethod();
        writer.begin_table(1);
        writer.tr().th("Name").th("Type").th("Default").th("Description");
        for (ParameterInfo info : handler.getParameterInfos())
        {
            if (info.getSource() == filter)
            {
                Parameter parameter = method.getParameters()[info.getIndex()];
                writer.tr();
                writer.td(info.getName());
                writer.td(parameter.getType().getName());
                writer.td(info.getDefaultValue());
                writer.td(getDescription(parameter));
                ;
            }
        }
        writer.end_table();
    }

    private void writeInputParameterInfos(HtmlWriter writer, AjaxButton button, String heading, RequestHandler handler, ParameterSource filter)
    {
        if (Arrays.stream(handler.getParameterInfos()).filter(info ->
        {
            return info.getSource() == filter;
        }).count() == 0)
        {
            return;
        }

        writer.h4(heading);
        Method method = handler.getMethod();
        writer.begin_table(1);
        writer.tr().th("Name").th("Type").th("Default").th("Value").th("Description");
        for (ParameterInfo info : handler.getParameterInfos())
        {
            if (info.getSource() == filter)
            {
                String name = info.getName();
                Parameter parameter = method.getParameters()[info.getIndex()];
                writer.tr();
                writer.td(name);
                writer.td(parameter.getType().getName());
                writer.td(info.getDefaultValue());
                String key = filter.toString() + name;
                if (parameter.getType() == boolean.class)
                {
                    button.val(key, key);
                    writer.td(writer.inner().input_checkbox(key, null, (boolean) info.getDefaultValue()));
                }
                else
                {
                    button.val(key, key);
                    writer.td(writer.inner().input_text(20, key, info.getDefaultValue() == null ? "" : info.getDefaultValue().toString()));
                }
                writer.td(getDescription(parameter));
                ;

            }
        }
        writer.end_table();
    }

    private ParameterInfo findContentParameter(RequestHandler requestHandler)
    {
        for (ParameterInfo info : requestHandler.getParameterInfos())
        {
            if (info.getSource() == ParameterSource.CONTENT)
            {
                Method method = requestHandler.getMethod();
                return info;
            }
        }
        return null;
    }

    static public class ParameterWriter
    {
        final ArrayList<Class<?>> usedClasses = new ArrayList<>();
        final HashSet<String> shownClasses = new HashSet<>();
        final HtmlWriter writer;

        ParameterWriter(HtmlWriter writer)
        {
            this.writer = writer;
        }

        void write(Class<?> type)
        {
            if (this.shownClasses.contains(type.getName()))
            {
                return;
            }
            this.shownClasses.add(type.getName());

            this.writer.h4("Type: " + type.getName());
            Description description = type.getAnnotation(Description.class);
            if (description != null)
            {
                this.writer.text(description.value());
            }
            writer.begin_sortableTable(1);
            writer.tr().th("Type").th("Name").th("Description");
            for (Field field : type.getDeclaredFields())
            {
                int modifiers = field.getModifiers();
                if (Modifier.isTransient(modifiers))
                {
                    continue;
                }
                if (Modifier.isStatic(modifiers))
                {
                    continue;
                }
                Class<?> fieldType = field.getType();

                writer.tr();
                if (fieldType.isArray())
                {
                    writer.td(Utils.escapeHtml(fieldType.getComponentType().getName() + "[]"));
                    fieldType = fieldType.getComponentType();
                }
                else
                {
                    writer.td(Utils.escapeHtml(fieldType.getName()));
                }
                writer.td(field.getName());
                description = field.getAnnotation(Description.class);
                if (description != null)
                {
                    writer.td(description.value());
                }
                else
                {
                    writer.td();
                }
                if (fieldType.isPrimitive())
                {
                    continue;
                }
                if (fieldType == Number.class)
                {
                    continue;
                }
                if (fieldType == String.class)
                {
                    continue;
                }
                this.usedClasses.add(fieldType);
            }
            writer.end_table();

            if (this.usedClasses.size() > 0)
            {
                Class<?> next = this.usedClasses.remove(0);
                write(next);
            }
        }
    }

    @GET
    @Path("/operator/httpServer/info")
    public Response<OperationContentResult> info(@QueryParam("key") String key) throws Throwable
    {
        HtmlWriter writer = new HtmlWriter();
        RequestHandler requestHandler = this.serverApplication.getOperatorServer().getRequestHandler(key);

        Map<Integer, CountAverageRateMeter> meters = requestHandler.getStatusMeters();
        if (meters.size() > 0)
        {
            writer.h3("Durations");
            writer.begin_table(1);
            writer.tr(writer.inner().th("Code", "Status Code").th("Count").th("Rate", "per second").th("Average", "Milliseconds").th("StdDev", "Standard Deviation").th("Total",
                    "Total duration in milliseconds"));
            for (Entry<Integer, CountAverageRateMeter> entry : meters.entrySet())
            {
                CountAverageRateMeter meter = entry.getValue();
                AverageAndRate averageAndRate = meter.getCountAverageRate(this.rateSamplingDuration);
                if (averageAndRate != null)
                {
                    writer.tr(writer.inner().td(entry.getKey()).td(meter.getCount()).td(DOUBLE_FORMAT.format(averageAndRate.getRate())).td(DOUBLE_FORMAT.format(averageAndRate.getAverage() / 1.0e6))
                            .td(DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation() / 1.0e6)).td(DOUBLE_FORMAT.format(meter.getTotal() / 1.0e6)));
                }
                else
                {
                    writer.tr(writer.inner().td(entry.getKey()).td(meter.getCount()).td(0).td(0).td(0).td(DOUBLE_FORMAT.format(meter.getTotal() / 1.0e6)));

                }
            }
            writer.end_table();
        }

        long requestUncompressed = 0;
        long requestCompressed = 0;
        long responseUncompressed = 0;
        long responseCompressed = 0;

        writer.h3("Content Sizes");
        writer.begin_table(1);
        writer.tr(writer.inner().th().th("Average", "Average bytes").th("StDev", "Standard Deviation").th("Total Bytes").th("KB").th("MB").th("GB").th("TB"));
        CountAverageRateMeter requestMeter = requestHandler.getRequestUncompressedContentSizeMeter();
        AverageAndRate averageAndRate = requestMeter.getCountAverageRate(this.rateSamplingDuration);
        if (averageAndRate != null)
        {
            long total = requestUncompressed = requestMeter.getTotal();
            writer.tr(writer.inner().td("Request uncompressed content").td(DOUBLE_FORMAT.format(averageAndRate.getAverage())).td(DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation())).td(total)
                    .td(total / 1024).td(total / 1024 / 1024).td(total / 1024 / 1024 / 1024).td(total / 1024 / 1024 / 1024 / 1024));
        }
        else
        {
            long total = requestUncompressed = requestMeter.getTotal();
            writer.tr(writer.inner().td("Request uncompressed content").td(0).td(0).td(total).td(total / 1024).td(total / 1024 / 1024).td(total / 1024 / 1024 / 1024)
                    .td(total / 1024 / 1024 / 1024 / 1024));

        }

        CountAverageRateMeter compressedRequestMeter = requestHandler.getRequestCompressedContentSizeMeter();
        averageAndRate = compressedRequestMeter.getCountAverageRate(this.rateSamplingDuration);
        if (averageAndRate != null)
        {
            long total = requestCompressed = compressedRequestMeter.getTotal();
            writer.tr(writer.inner().td("Request compressed content").td(DOUBLE_FORMAT.format(averageAndRate.getAverage())).td(DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation())).td(total)
                    .td(total / 1024).td(total / 1024 / 1024).td(total / 1024 / 1024 / 1024).td(total / 1024 / 1024 / 1024 / 1024));
        }
        else
        {
            long total = requestCompressed = compressedRequestMeter.getTotal();
            writer.tr(writer.inner().td("Request compressed content").td(0).td(0).td(total).td(total / 1024).td(total / 1024 / 1024).td(total / 1024 / 1024 / 1024)
                    .td(total / 1024 / 1024 / 1024 / 1024));

        }

        CountAverageRateMeter responseMeter = requestHandler.getResponseUncompressedContentSizeMeter();
        averageAndRate = responseMeter.getCountAverageRate(this.rateSamplingDuration);
        if (averageAndRate != null)
        {
            long total = responseUncompressed = responseMeter.getTotal();
            writer.tr(writer.inner().td("Response uncompressed content").td(DOUBLE_FORMAT.format(averageAndRate.getAverage())).td(DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation())).td(total)
                    .td(total / 1024).td(total / 1024 / 1024).td(total / 1024 / 1024 / 1024).td(total / 1024 / 1024 / 1024 / 1024));
        }
        else
        {
            long total = responseUncompressed = responseMeter.getTotal();
            writer.tr(writer.inner().td("Response uncompressed content").td(0).td(0).td(total).td(total / 1024).td(total / 1024 / 1024).td(total / 1024 / 1024 / 1024)
                    .td(total / 1024 / 1024 / 1024 / 1024));

        }

        CountAverageRateMeter compressedResponseMeter = requestHandler.getResponseCompressedContentSizeMeter();
        averageAndRate = compressedResponseMeter.getCountAverageRate(this.rateSamplingDuration);
        if (averageAndRate != null)
        {
            long total = responseCompressed = compressedResponseMeter.getTotal();
            writer.tr(writer.inner().td("Response compressed content").td(DOUBLE_FORMAT.format(averageAndRate.getAverage())).td(DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation())).td(total)
                    .td(total / 1024).td(total / 1024 / 1024).td(total / 1024 / 1024 / 1024).td(total / 1024 / 1024 / 1024 / 1024));
        }
        else
        {
            long total = responseCompressed = compressedResponseMeter.getTotal();
            writer.tr(writer.inner().td("Response compressed content").td(0).td(0).td(total).td(total / 1024).td(total / 1024 / 1024).td(total / 1024 / 1024 / 1024)
                    .td(total / 1024 / 1024 / 1024 / 1024));

        }
        writer.end_table();

        writer.h3("Compression");
        writer.begin_table(1);
        writer.tr(writer.inner().th("Request").th("Response"));
        writer.begin_tr();
        if (requestUncompressed != 0)
        {
            writer.td(DOUBLE_FORMAT.format((double) requestCompressed / requestUncompressed));
        }
        else
        {
            writer.td("-");
        }
        if (responseUncompressed != 0)
        {
            writer.td(DOUBLE_FORMAT.format((double) responseCompressed / responseUncompressed));
        }
        else
        {
            writer.td("-");
        }
        writer.end_tr();
        writer.end_table();

        return OperationContentResult.respond(writer, "Method: " + key);
    }

    static class ContentWriterList
    {
        ContentWriter<?> contentWriter;
        ArrayList<String> types;

        public ContentWriterList(ContentWriter<?> contentWriter)
        {
            this.contentWriter = contentWriter;
            this.types = new ArrayList<>();
        }
    }


    private String generateClassDefinitions(String server,String namespace,int columns,InteropTarget target) throws Exception
    {
        HttpServer httpServer=getHttpServer(server);
        HashMap<String,Class<?>> roots=new HashMap<>();
        for (RequestHandler requestHandler:httpServer.getRequestHandlers())
        {
            Method method = requestHandler.getMethod();
            ParameterInfo contentParameterInfo = findContentParameter(requestHandler);

            if (contentParameterInfo!=null)
            {
                for (ContentReader<?> reader:requestHandler.getContentReaders().values())
                {
                    if (reader instanceof JSONContentReader)
                    {
                        Class<?> contentType=contentParameterInfo.getType();
                        roots.put(contentType.getCanonicalName(),contentType);
                        break;
                    }
                }
            }
    
            Class<?> returnType = method.getReturnType();
            Type innerReturnType = null;
            if (returnType != void.class)
            {
                for (ContentWriter<?> writer:requestHandler.getContentWriters().values())
                {
                    if (writer instanceof JSONContentWriter)
                    {
                        if (returnType == Response.class)
                        {
                            ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
                            innerReturnType = type.getActualTypeArguments()[0];
                            String typeName = innerReturnType.getTypeName();
                            if (typeName.equals("?")==false)
                            {
                                returnType = (Class<?>) innerReturnType;
                            }
                        }
                        roots.put(returnType.getCanonicalName(),returnType);
                        break;
                    }
                }
            }
        }
        CSharpClassWriter classWriter=new CSharpClassWriter();
        String source=Utils.getLocalHostName();
        return classWriter.write(source,namespace, roots.values(),columns,target);
    }
    

    @GET
    @Path("/operator/httpServer/classDefinitions/{server}")
    public Response<OperationContentResult> classDefinitions(Context context,@PathParam("server") String server) throws Throwable
    {
        HtmlWriter writer = new HtmlWriter();
        writer.begin_form("/operator/httpServer/classDefinitions/download/"+server, "get");
        writer.begin_table();
        writer.tr().td("Target").td(":");
        writer.begin_td();
        Selection selection=new Selection("target",new Attribute("style", "width:100%;"));
        selection.options(InteropTarget.values());
        writer.insert(selection);
        writer.end_td();

        writer.tr().td("Namespace").td(":");
        writer.begin_td();
        writer.input_text(40,"namespace", this.serverApplication.getName());
        writer.end_td();

        writer.tr().td("Columns").td(":");
        writer.begin_td();
        writer.input_text(40,"columns", "120");
        writer.end_td();
        writer.end_table();
        
        writer.begin_table();
        writer.td(writer.inner().input_submit("Download"));
        
        AjaxButton button = new AjaxButton("button", "Preview", "/operator/httpServer/classDefinitions/preview/"+server);
        button.type("post");
        button.val("namespace", "namespace");
        button.val("target", "target");
        button.val("columns", "columns");
        button.async(true);
        writer.td(writer.inner().insert(button));
        writer.end_td();
        writer.end_table();
        writer.end_form();
        writer.hr();
        
        writer.div(null, new Attribute("id", "result"));
        

        return OperationContentResult.respond(writer, "Generate Interop Classes: " + server);
    }
    
    @POST
    @Path("/operator/httpServer/classDefinitions/preview/{server}")
    @ContentWriters(AjaxQueryContentWriter.class)
    public AjaxQueryResult previewClassDefinitions(Trace parent, Context context, @PathParam("server") String server,@QueryParam("namespace") String namespace,@QueryParam ("columns") int columns,@QueryParam("target") InteropTarget target) throws Throwable
    {
        AjaxQueryResult result = new AjaxQueryResult();
        HtmlWriter writer = new HtmlWriter();
        String text=generateClassDefinitions(server, namespace, columns, target);
        int rows=Utils.occurs(text, "\n")+2;
        if (rows>50)
        {
            rows=50;
        }
        writer.textarea(text, new Attribute("style","width:100%"),new Attribute("rows",rows));
        result.put("result", writer.toString());
        return result;
    }

    @GET
    @Path("/operator/httpServer/classDefinitions/download/{server}")
    
    public Response<String> downloadClassDefinitions(Context context,@PathParam("server") String server,@QueryParam("namespace") String namespace,@QueryParam ("columns") int columns,@QueryParam("target") InteropTarget target) throws Throwable
    {
        switch (target)
        {
            case CSHARP_PLAIN:
            {
                String text=generateClassDefinitions(server, namespace, columns, target);
                String filename=namespace.replace(".", "_")+".cs";
                Response<String> response=new Response<>(text);
                response.addHeader("Content-Disposition","attachment;filename="+filename);
                return response;
            }
            default:
                break;
            
        }
        return new Response<String>(HttpStatus.BAD_REQUEST_400);
    }
    

    @Description("Displays information and documentation of method.")
    @GET
    @Path("/operator/httpServer/method/{server}")
    public Response<OperationContentResult> method(Context context,@PathParam("server") String server,@QueryParam("key") String key) throws Throwable
    {
        HtmlWriter writer = new HtmlWriter();
        writer.h2("Method: "+key);
        HttpServer httpServer=getHttpServer(server);
        writer.p("Preferred Port: "+httpServer.getPreferredPort());
        HttpServletRequest request=context.getHttpServletRequest();
        RequestHandler requestHandler = httpServer.getRequestHandler(key);

        Method method = requestHandler.getMethod();

        String text = getDescription(method);
        if (text != null)
        {
            writer.text(text);
        }

        writeParameterInfos(writer, "Query Parameters", requestHandler, ParameterSource.QUERY);
        writeParameterInfos(writer, "Path Parameters", requestHandler, ParameterSource.PATH);
        writeParameterInfos(writer, "Header Parameters", requestHandler, ParameterSource.HEADER);
        writeParameterInfos(writer, "Cookie Parameters", requestHandler, ParameterSource.COOKIE);

        ParameterInfo contentParameterInfo = findContentParameter(requestHandler);
        if (contentParameterInfo != null)
        {
            writer.h3("Content Parameter");

            Parameter contentParameter = method.getParameters()[contentParameterInfo.getIndex()];
            ParameterWriter parameterWriter = new ParameterWriter(writer);
            parameterWriter.write(contentParameter.getType());

            if (requestHandler.getContentReaders().size() > 0)
            {

                writer.h4("Content Readers");
                for (ContentReader<?> contentReader : requestHandler.getContentReaders().values())
                {
                    writer.begin_table(1);
                    writer.tr().th("Media Type").th("Class");
                    writer.tr().td(contentReader.getMediaType()).td(contentReader.getClass().getName());
                    writer.end_table();

                    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                    contentReader.writeSchema(byteOutputStream, contentParameter.getType());
                    String schema = new String(byteOutputStream.toByteArray());

                    byteOutputStream.reset();
                    contentReader.writeExample(byteOutputStream, contentParameter.getType());
                    String example = new String(byteOutputStream.toByteArray());
                    writer.br();
                    if ((schema.length() > 0) || (example.length() > 0))
                    {
                        writer.begin_table(1).tr();
                        int rows = 0;
                        int columns = 0;
                        if (schema.length() > 0)
                        {
                            writer.th("Schema");
                            int r = Utils.occurs(schema, "\r");
                            if (r > rows)
                            {
                                rows = r;
                            }
                            columns++;
                        }
                        if (example.length() > 0)
                        {
                            writer.th("Example");
                            int r = Utils.occurs(example, "\r");
                            if (r > rows)
                            {
                                rows = r;
                            }
                            columns++;
                        }
                        rows += 1;
                        if (rows > 25)
                        {
                            rows = 25;
                        }
                        writer.tr();
                        int cols = 160 / columns;

                        if (schema.length() > 0)
                        {
                            writer.td(writer.inner().textarea(schema, true, rows, cols));
                        }
                        if (example.length() > 0)
                        {
                            writer.td(writer.inner().textarea(example, true, rows, cols));
                        }
                        writer.end_table();
                    }

                }
            }
        }

        Class<?> returnType = method.getReturnType();
        Type innerReturnType = null;
        if (returnType != void.class)
        {
            writer.h2("Response");
            ParameterWriter parameterWriter = new ParameterWriter(writer);
            if (returnType == Response.class)
            {
                ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
                innerReturnType = type.getActualTypeArguments()[0];
                String typeName = innerReturnType.getTypeName();
                if (typeName.equals("?"))
                {
                    parameterWriter.write(returnType);
                }
                else
                {
                    returnType = (Class<?>) innerReturnType;
                    innerReturnType = null;
                    parameterWriter.write(returnType);
                }
            }
            else
            {
                parameterWriter.write(returnType);
            }

            if (requestHandler.getContentWriters().size() > 0)
            {
                HashMap<String, ContentWriterList> lists = new HashMap<>();
                for (Entry<String, ContentWriter<?>> entry : requestHandler.getContentWriters().entrySet())
                {
                    // writer.tr().td(entry.getKey()).td(entry.getValue().getClass().getName());
                    ContentWriterList types = lists.get(entry.getValue().getMediaType());
                    if (types == null)
                    {
                        types = new ContentWriterList(entry.getValue());
                        lists.put(entry.getValue().getMediaType(), types);
                    }
                    types.types.add(entry.getKey());
                }

                writer.h4("Content Writers");
                for (ContentWriterList list : lists.values())
                {
                    writer.begin_table(1);
                    writer.tr().th("Media Type").th("Accept Types").th("Class");
                    writer.tr().td(list.contentWriter.getMediaType()).td(Utils.combine(list.types, ", ")).td(list.contentWriter.getClass().getName());
                    writer.end_table();

                    if (innerReturnType == null)
                    {
                        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                        list.contentWriter.writeSchema(byteOutputStream, returnType);
                        String schema = new String(byteOutputStream.toByteArray());

                        byteOutputStream.reset();
                        list.contentWriter.writeExample(byteOutputStream, returnType);
                        String example = new String(byteOutputStream.toByteArray());
                        writer.br();
                        if ((schema.length() > 0) || (example.length() > 0))
                        {
                            writer.begin_table(1).tr();
                            int rows = 0;
                            int columns = 0;
                            if (schema.length() > 0)
                            {
                                writer.th("Schema");
                                int r = Utils.occurs(schema, "\r");
                                if (r > rows)
                                {
                                    rows = r;
                                }
                                columns++;
                            }
                            if (example.length() > 0)
                            {
                                writer.th("Example");
                                int r = Utils.occurs(example, "\r");
                                if (r > rows)
                                {
                                    rows = r;
                                }
                                columns++;
                            }
                            rows += 1;
                            if (rows > 25)
                            {
                                rows = 25;
                            }
                            writer.tr();
                            int cols = 160 / columns;

                            if (schema.length() > 0)
                            {
                                writer.td(writer.inner().textarea(schema, true, rows, cols));
                            }
                            if (example.length() > 0)
                            {
                                writer.td(writer.inner().textarea(example, true, rows, cols));
                            }
                            writer.end_table();
                        }
                    }

                }
            }
        }

        if (requestHandler.getContentDecoders().size() > 0)
        {
            writer.h2("Content Decoders");
            writer.begin_table(1);
            writer.tr().th("Content-Encoding").th("Decoder").th("Class");
            for (Entry<String, ContentDecoder> entry : requestHandler.getContentDecoders().entrySet())
            {
                writer.tr().td(entry.getValue().getCoding()).td(entry.getKey()).td(entry.getValue().getClass().getName());
            }
            writer.end_table();
        }
        if (requestHandler.getContentEncoders().size() > 0)
        {
            writer.h2("Content Encoders");
            writer.begin_table(1);
            writer.tr().th("Accept-Encoding").th("Encoder").th("Class");
            for (Entry<String, ContentEncoder> entry : requestHandler.getContentEncoders().entrySet())
            {
                writer.tr().td(entry.getValue().getCoding()).td(entry.getKey()).td(entry.getValue().getClass().getName());
            }
            writer.end_table();
        }
        if (requestHandler.getFilters().length > 0)
        {
            writer.h2("Filters");
            writer.begin_table(1);
            writer.tr().th("Filter");
            for (Filter filter : requestHandler.getFilters())
            {
                writer.tr().td(filter.getClass().getName());
            }
            writer.end_table();
        }
        writer.h2("Class Method");

        writer.text(Utils.escapeHtml(method.toGenericString())+";");
        writer.br();

        String httpMethod = requestHandler.getHttpMethod();

        // if (requestHandler.getHttpMethod().equals("GET"))
        {
            writer.h2("Execute: " + key);
            AjaxButton button = new AjaxButton("button", "Execute", "/operator/httpServer/method/execute/"+server);
            button.parameter("key", key);
            writeInputParameterInfos(writer, button, "Query Parameters", requestHandler, ParameterSource.QUERY);
            writeInputParameterInfos(writer, button, "Path Parameters", requestHandler, ParameterSource.PATH);
            writeInputParameterInfos(writer, button, "Header Parameters", requestHandler, ParameterSource.HEADER);
            writeInputParameterInfos(writer, button, "Cookie Parameters", requestHandler, ParameterSource.COOKIE);
            button.async(false);

            if ("POST".equals(httpMethod) || "PUT".equals(httpMethod)|| "PATCH".equals(httpMethod))
            {
                for (ParameterInfo info:requestHandler.getParameterInfos())
                {
                    if (info.getSource()==ParameterSource.CONTENT)
                    {
                        Collection<ContentReader<?>> readers = requestHandler.getContentReaders().values();
                        if (readers.size() > 0)
                        {
                            writer.text("Request Content:");
                            writer.textarea("", new Attribute("id", "contentText"), new Attribute("style", "width:100%;height:100px;"));

                            button.val("contentText", "contentText");
                        }
                    }
                }

            }
            writer.begin_table(new Attribute("style","width:100%"));
            writer.col("180");
            writer.col("10");
            writer.col("auto");
            writer.tr();
            writer.td("Additional Headers").td(":");
            writer.begin_td().input_text("headers",new Attribute("style","width:100%")).end_td();
            button.val("headers", "headers");
            if ("POST".equals(httpMethod) || "PUT".equals(httpMethod))
            {
                Collection<ContentReader<?>> readers = requestHandler.getContentReaders().values();
                if (readers.size() > 0)
                {
                    HashSet<String> set=new HashSet<>();
                    for (ContentReader<?> reader : readers)
                    {
                        set.add(reader.getMediaType());
                    }
                    writer.tr();
                    writer.td("Accept").td(":");;
                    writer.begin_td();
                    writer.begin_select("accept",new Attribute("style", "width:100%;"));
                    for (String type:set)
                    {
                        writer.option(type);
                    }
                    writer.end_select();
                    writer.end_td();
                    button.val("accept", "accept");
                }
            }
            Collection<ContentWriter<?>> writers = requestHandler.getContentWriters().values();

            if (writers.size() > 0)
            {
                HashSet<String> set=new HashSet<>();
                for (ContentWriter<?> contentWriter : writers)
                {
                    set.add(contentWriter.getMediaType());
                }
                writer.tr();
                writer.td("Response Content-Type").td(":");
                writer.begin_td();
                writer.begin_select("contentType",new Attribute("style", "width:100%;"));
                for (String type:set)
                {
                    writer.option(type);
                }
                writer.end_select();
                writer.end_td();
            }
            writer.end_table();
            
            writer.hr();
            writer.insert(button);
            writer.div(null, new Attribute("id", "result"));

        }
  
        /* testing
        writer.hr();
        writer.text("Schema: ");
        writer.a("JSON", "/operator/httpServer/api?key="+key);
*/
        return OperationContentResult.respond(writer, "Methods: " + server);
    }

    @GET
    @Path("/operator/httpServer/method/execute/{server}")
    @ContentWriters(AjaxQueryContentWriter.class)
    public AjaxQueryResult execute(Trace parent, Context context, @PathParam("server") String server,@QueryParam("key") String key) throws Throwable
    {
        try
        {
            HttpServer httpServer=getHttpServer(server);
            HttpServletRequest request = context.getHttpServletRequest();
            String endPoint = request.getHeader("Referer");
            int index=endPoint.lastIndexOf(':');
            if (index>0)
            {
                endPoint=endPoint.substring(0,index);
            }
            endPoint=endPoint+":"+httpServer.getPreferredPort();
            ArrayList<Header> headers = new ArrayList<>();
            ArrayList<Cookie> cookies = new ArrayList<>();
            String[] methodSchema = Utils.split(key, ' ');
            String method = methodSchema[0];
            String schema = methodSchema[1];
            String[] fragments = Utils.split(schema, '/');

            StringBuilder pathAndQuery = new StringBuilder();
            for (int i = 1; i < fragments.length; i++)
            {
                String fragment = fragments[i];
                pathAndQuery.append("/");
                if (fragment.startsWith("{"))
                {
                    String name = ParameterSource.PATH + fragment.substring(1, fragment.length() - 2);
                    pathAndQuery.append(request.getParameter(name));
                }
                else
                {
                    pathAndQuery.append(fragment);
                }
            }
            String prefix = "?";
            for (Enumeration<String> enumerator = request.getParameterNames(); enumerator.hasMoreElements(); enumerator.hasMoreElements())
            {
                String mangledName = enumerator.nextElement();
                if (mangledName.startsWith(ParameterSource.QUERY.toString()))
                {
                    String name = mangledName.substring(ParameterSource.QUERY.toString().length());
                    pathAndQuery.append(prefix);
                    pathAndQuery.append(name);
                    pathAndQuery.append("=");
                    pathAndQuery.append(request.getParameter(mangledName));
                    prefix = "&";
                }
                else if (mangledName.startsWith(ParameterSource.HEADER.toString()))
                {
                    String name = mangledName.substring(ParameterSource.QUERY.toString().length());
                    headers.add(new Header(name, request.getParameter(mangledName)));
                }
                else if (mangledName.startsWith(ParameterSource.COOKIE.toString()))
                {
                    String name = mangledName.substring(ParameterSource.QUERY.toString().length());
                    cookies.add(new Cookie(name, request.getParameter(mangledName)));
                }
            }
            String additionalHeaders=request.getParameter("headers");
            if (Strings.isNullOrEmpty(additionalHeaders)==false)
            {
                String[] nameValues=Utils.split(additionalHeaders,';');
                for (String nameValue:nameValues)
                {
                    String[] parts=Utils.split(nameValue, ':');
                    if (parts.length==2)
                    {
                        headers.add(new Header(parts[0].trim(),parts[1].trim()));
                    }
                }
            }

            TextClient textClient = new TextClient(this.serverApplication.getTraceManager(), this.serverApplication.getLogger(), endPoint);
            int statusCode;
            TextResponse response=null;
            double duration = 0;
            if (method.equals("POST"))
            {
                String accept = request.getParameter("accept");
                String contentText = request.getParameter("contentText");
                if (Strings.isNullOrEmpty(accept)==false)
                {
                    headers.add(new Header("Content-Type", accept));
                }
                try (Trace trace = new Trace(this.serverApplication.getTraceManager(), parent, "Execute"))
                {
                    response = textClient.post(trace, null, pathAndQuery.toString(), contentText, headers.toArray(new Header[headers.size()]));
                    statusCode=response.getStatusCode();
                    duration = trace.getDuration();
                }
            }
            else if (method.equals("GET"))
            {
                try (Trace trace = new Trace(this.serverApplication.getTraceManager(), parent, "Execute"))
                {
                    response = textClient.get(trace, null, pathAndQuery.toString(), headers.toArray(new Header[headers.size()]));
                    statusCode=response.getStatusCode();
                    duration = trace.getDuration();
                }
            }
            else if (method.equals("PUT"))
            {
                String accept = request.getParameter("accept");
                String contentText = request.getParameter("contentText");
                if (Strings.isNullOrEmpty(accept)==false)
                {
                    headers.add(new Header("Content-Type", accept));
                }
                try (Trace trace = new Trace(this.serverApplication.getTraceManager(), parent, "Execute"))
                {
                    statusCode=textClient.put(trace, null, pathAndQuery.toString(), contentText, headers.toArray(new Header[headers.size()]));
                    duration = trace.getDuration();
                }
            }
            else if (method.equals("PATCH"))
            {
                String accept = request.getParameter("accept");
                String contentText = request.getParameter("contentText");
                if (Strings.isNullOrEmpty(accept)==false)
                {
                    headers.add(new Header("Content-Type", accept));
                }
                try (Trace trace = new Trace(this.serverApplication.getTraceManager(), parent, "Execute"))
                {
                    statusCode=textClient.patch(trace, null, pathAndQuery.toString(), contentText, headers.toArray(new Header[headers.size()]));
                    duration = trace.getDuration();
                }
            }
            else if (method.equals("DELETE"))
            {
                String accept = request.getParameter("accept");
                if (Strings.isNullOrEmpty(accept)==false)
                {
                    headers.add(new Header("Content-Type", accept));
                }
                try (Trace trace = new Trace(this.serverApplication.getTraceManager(), parent, "Execute"))
                {
                    statusCode=textClient.delete(trace, null, pathAndQuery.toString(), headers.toArray(new Header[headers.size()]));
                    duration = trace.getDuration();
                }
            }
            else
            {
                AjaxQueryResult result = new AjaxQueryResult();
                HtmlWriter writer = new HtmlWriter();
                writer.h3("Not implemented. Method=" + method);
                result.put("result", writer.toString());
                return result;
            }
            AjaxQueryResult result = new AjaxQueryResult();
            HtmlWriter writer = new HtmlWriter();
            writer.hr();
            writer.h3("Result");

            TableList tableList=new TableList();
            tableList.row("Time",Utils.millisToLocalDateTimeString(System.currentTimeMillis()));
            tableList.row("Duration", duration * 1000 + " ms");
            tableList.row("Status Code",statusCode);
            writer.insert(tableList);
            if (response!=null)
            {
                if (response.getHeaders().length > 0)
                {
                    writer.h4("Response Headers");
                    tableList=new TableList();
                    for (Header header : response.getHeaders())
                    {
                        tableList.row(header.getName(),header.getValue());
                    }
                    writer.insert(tableList);
                }
                if (response.getText().length() > 0)
                {
                    writer.p();
                    writer.button("+", new Attribute("id", "expand"),
                            new Attribute("onclick", "{var textContent=document.getElementById('textContent');textContent.style.height=(textContent.scrollHeight)+'px';}"));
                    writer.text(" ");
    
                    writer.text("Content:");
                    writer.textarea(response.getText(), new Attribute("readonly", true), new Attribute("id", "textContent"), new Attribute("rows", "4"), new Attribute("style", "width:100%;"));
                }
            }
            result.put("result", writer.toString());
            return result;
        }
        catch (Throwable t)
        {
            AjaxQueryResult result = new AjaxQueryResult();
            HtmlWriter writer = new HtmlWriter();
            writer.h3("Internal Execution Exception");
            writer.textarea(Utils.getStrackTraceAsString(t), new Attribute("readonly", true), new Attribute("id", "textContent"), new Attribute("rows", "10"), new Attribute("style", "width:100%;"));
            result.put("result", writer.toString());
            return result;
            
        }
    }

    @GET
    @Path("/")
    public Response<OperationContentResult> main() throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        writer.begin_table(1);
        writer.tr().td("Started").td(Utils.millisToLocalDateTimeString(this.serverApplication.getStartTime()));
        long now = System.currentTimeMillis();
        writer.tr().td("Current").td(Utils.millisToLocalDateTimeString(now));
        writer.tr().td("Uptime").td(Utils.millisToDurationString(now - this.serverApplication.getStartTime()));
        writer.end_table();
        return OperationContentResult.respond(writer, "Main");
    }

    private void writeSize(HtmlWriter writer, String label, long size)
    {
        writer.tr(writer.inner().td(label).td(size).td(size / 1024).td(size / 1024 / 1024).td(size / 1024 / 1024 / 1024));
    }

    private static DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#.###");

    private void writeSize(HtmlWriter writer, String label, double value)
    {
        writer.tr(writer.inner().td(label).td(DOUBLE_FORMAT.format(value)).td(DOUBLE_FORMAT.format(value / 1024)).td(DOUBLE_FORMAT.format(value / 1024 / 1024))
                .td(DOUBLE_FORMAT.format(value / 1024 / 1024 / 1024)));
    }

    private void write(HtmlWriter writer, String label, LevelMeter meter)
    {
        writer.tr(writer.inner().td(label).td(meter.getLevel()).td(meter.getMaximumLevel()).td(Utils.millisToLocalDateTime(meter.getHighestLevelTimeStamp())));
    }

    private void write(HtmlWriter writer, String label, CountMeter meter)
    {
        writer.tr(writer.inner().td(label).td(meter.getCount()).td().td());
    }

    @GET
    @Path("/operator/logging/status")
    public Response<OperationContentResult> loggingStatus() throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        LogDirectoryManager manager = this.serverApplication.getLogDirectoryManager();
        if (manager != null)
        {

            if (this.serverApplication.getLogQueue() instanceof JSONBufferedLZ4Queue)
            {
                JSONBufferedLZ4Queue sink = (JSONBufferedLZ4Queue) this.serverApplication.getLogQueue();
                writer.h3("Logger Worker Stats");
                writer.begin_table(1);
                writer.th("").th("").th("Max").th("Max Time");
                write(writer,"Thread Workers Used",sink.getThreadWorkerQueueInUseMeter());
                write(writer,"Waiting in Thread Workers",sink.getThreadWorkerQueueWaitingMeter());
                write(writer,"Stalled in Thread Workers",sink.getThreadWorkerQueueStalledMeter());
                write(writer,"Dropped in Thread Workers",sink.getThreadWorkerQueueDroppedMeter());
                write(writer,"Waiting in Source",sink.getWaitingMeter());
                write(writer,"Stalled in Source ",sink.getStalledMeter());
                write(writer,"Dropped in Source ",sink.getDroppedMeter());
                writer.end_table();
                
                writer.h3("Logger Usage");
                writer.begin_table(1);
                writer.th("").th("bytes").th("KB").th("MB").th("GB");
                writeSize(writer, "Write Rate (per second)", sink.getWriteRateMeter().sampleRate(this.rateSamplingDuration));
                writeSize(writer, "Written", sink.getWriteRateMeter().getCount());
                writer.end_table();
            }
            
            writer.h3("File Info");
            LogDirectoryInfo info = manager.getLogDirectoryInfo();
            writer.begin_table(1);
            writer.tr(writer.inner().td("Path").td(manager.getFullDirectoryPath()));
            writer.tr(writer.inner().td("Deletes due to directory size exceeded").td(manager.getDirectorySizeDeleteMeter().getCount()));
            writer.tr(writer.inner().td("Deletes due to maximum files exceeded").td(manager.getMaximumFilesDeleteMeter().getCount()));
            writer.tr(writer.inner().td("Deletes due to reserve space exceeded").td(manager.getReserveSpaceDeleteMeter().getCount()));
            writer.tr(writer.inner().td("File delete failures").td(manager.getFileDeleteFailedMeter().getCount()));
            writer.tr(writer.inner().td("Number of files").td(info.getFileCount()));
            writer.tr(writer.inner().td("Oldest file name").td(info.getOldestFileName()));
            writer.tr(writer.inner().td("Oldest file date").td(Utils.millisToLocalDateTimeString(info.getOldestFileDate())));
            writer.tr(writer.inner().td("Newest file name").td(info.getNewestFileName()));
            writer.tr(writer.inner().td("Newest file date").td(Utils.millisToLocalDateTimeString(info.getNewestFileDate())));
            writer.end_table();

            writer.h3("File Usage");
            writer.begin_table(1);
            writer.th("").th("bytes").th("KB").th("MB").th("GB");
            writeSize(writer, "Directory size", info.getDirectorySize());
            writeSize(writer, "Volume free space", info.getFreeSpace());
            writeSize(writer, "Volume free space", info.getFreeSpace());
            writeSize(writer, "Volume usable space", info.getUsableSpace());
            writeSize(writer, "Volume total space", info.getTotalSpace());
            if (info.getFileCount() > 0)
            {
                writeSize(writer, "Oldest file size", info.getOldestFileSize());
                writeSize(writer, "Newest file size", info.getNewestFileSize());
            }

            if (info.getThrowable() != null)
            {
                writer.tr(writer.inner().td("Directory Exception").td(Utils.toString(info.getThrowable())));
            }
            
            writer.end_table();

            
            
        }
        return OperationContentResult.respond(writer, "Logging Status");
    }

    @GET
    @Path("/content/{+}")
    public void content(@PathParam(PathParam.AT_LEAST_ONE_SEGMENT) String file, Context context, Trace trace) throws Throwable
    {
        HttpServletResponse response = context.getHttpServletResponse();
        byte[] bytes = this.serverApplication.getFileCache().get(trace, file);
        context.setHandled(true);
        if (bytes == null)
        {
            if (Testing.ENABLED)
            {
                TestTraceClient.clientLog(file + " not found");
            }
            response.setStatus(HttpStatus.NOT_FOUND_404);
            return;
        }
        String contentType = this.serverApplication.getTypeMappings().getContentType(file);
        if (contentType != null)
        {
            response.setContentType(contentType);
        }
        response.setStatus(HttpStatus.OK_200);
        response.getOutputStream().write(bytes);
    }

    @GET
    @Path("/cache/{+}")
    public void cache(@PathParam(PathParam.AT_LEAST_ONE_SEGMENT) String file, Context context, Trace trace) throws Throwable
    {
        HttpServletResponse response = context.getHttpServletResponse();
        context.setHandled(true);

        byte[] bytes;
        try
        {
            bytes = this.serverApplication.getFileCache().get(trace, file);
        }
        catch (Throwable t)
        {
            if (Testing.ENABLED)
            {
                TestTraceClient.clientLog(file + " not found");
            }
            response.setStatus(HttpStatus.NOT_FOUND_404);
            return;
        }
        String contentType = this.serverApplication.getTypeMappings().getContentType(file);
        if (contentType != null)
        {
            response.setContentType(contentType);
        }
        response.setHeader("Cache-Control",
                (this.cacheControlValue == null || this.cacheControlValue.length() == 0) ? "max-age=" + this.cacheMaxAge : this.cacheControlValue + ",max-age=" + this.cacheMaxAge);
        response.setStatus(HttpStatus.OK_200);
        response.getOutputStream().write(bytes);
    }
}
