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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.eclipse.jetty.http.HttpStatus;
import org.nova.annotations.Description;
import org.nova.concurrent.Future;
import org.nova.concurrent.TimerTask;
import org.nova.configuration.Configuration;
import org.nova.configuration.ConfigurationItem;
import org.nova.core.Utils;
import org.nova.html.operator.Menu;
import org.nova.html.operator.OperatorResultWriter;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.fieldset;
import org.nova.html.tags.form_get;
import org.nova.html.tags.form_post;
import org.nova.html.tags.hr;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_hidden;
import org.nova.html.tags.input_number;
import org.nova.html.tags.input_submit;
import org.nova.html.tags.input_text;
import org.nova.html.tags.legend;
import org.nova.html.tags.meta;
import org.nova.html.tags.p;
import org.nova.html.tags.style;
import org.nova.html.tags.td;
import org.nova.html.tags.textarea;
import org.nova.html.tags.tr;
import org.nova.html.widgets.AjaxButton;
import org.nova.html.widgets.AjaxQueryResult;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.html.widgets.BasicPage;
import org.nova.html.widgets.DataTable;
import org.nova.html.widgets.Head;
import org.nova.html.widgets.MenuBar;
import org.nova.html.widgets.NameInputValueList;
import org.nova.html.widgets.NameValueList;
import org.nova.html.widgets.Panel;
import org.nova.html.widgets.Row;
import org.nova.html.widgets.SelectOptions;
import org.nova.html.widgets.Table;
import org.nova.html.widgets.w3c.Accordion;
import org.nova.html.widgets.w3c.VerticalMenu;
import org.nova.http.Header;
import org.nova.http.client.HttpClientConfiguration;
import org.nova.http.client.HttpClientFactory;
import org.nova.http.client.PathAndQueryBuilder;
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
import org.nova.http.server.HtmlContentWriter;
import org.nova.html.elements.Element;
import org.nova.html.elements.FormElement;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.elements.InnerElement;
import org.nova.html.enums.http_equiv;
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
import org.nova.metrics.MeterSnapshot;
import org.nova.metrics.RateMeter;
import org.nova.metrics.RateMeterBox;
import org.nova.operations.OperatorVariable;
import org.nova.security.Vault;
import org.nova.test.Testing;
import org.nova.testing.TestTraceClient;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceNode;
import org.nova.tracing.TraceStats;

import com.google.common.base.Strings;

@Description("Handlers for server operator pages")
@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({ JSONContentReader.class, JSONPatchContentReader.class })
@ContentWriters({OperatorResultWriter.class, HtmlContentWriter.class, HtmlElementWriter.class})
public class ServerOperatorPages
{
    static public class OperatorTable extends DataTable
    {
        public OperatorTable(Head head)
        {
            super(head,null);
            this.lengthMenu(-1,20,30,40,50);
        }
    }

    static public class WideTable extends Table
    {
        public WideTable(Head head)
        {
            super();
            
            head.add(WideTable.class.getCanonicalName(),new style().addInner("table.widetable {border-collapse:collapse;width:100%;} .widetable thead {background-color:#eee;} .widetable td{border:1px solid #888;padding:4px;} "));
            class_("widetable");
        }
    }
    static public class Level1Panel extends Panel
    {
        public Level1Panel(Head head,String title)
        {
            super(head,null,title);
        }
    }
    static public class Level2Panel extends Level1Panel
    {
        public Level2Panel(Head head,String title)
        {
            super(head,title);
            style("padding:4px;");
            this.heading().style("background-color:#ddd;color:#000;text-align:left;padding:2px 2px 2px 4px;");
            this.content().style("padding:0px;");
        }
    }
    static public class Level3Panel extends Level1Panel
    {
        public Level3Panel(Head head,String title)
        {
            super(head,title);
            //top right bottom left
            style("padding:4px;");
            this.heading().style("background-color:#fff;color:#000;text-align:left;font-weight:normal;padding:2px 2px 2px 2px;");
            this.content().style("padding:2px 2px 2px 2px;");
        }
    }
    
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

        MenuBar menuBar=serverApplication.getMenuBar();
        menuBar.add("/operator/application/configuration","Application","Configuration");
        menuBar.add("/operator/application/futures","Application","Futures");
        menuBar.add("/operator/application/timers","Application","Timer Tasks");
        menuBar.add("/operator/application/meters/categories","Application","Category Meters");
        menuBar.add("/operator/application/meters/all","Application","All Meters");

        menuBar.add("/operator/tracing/activeStats","Tracing","Active Trace Stats");
        menuBar.add("/operator/tracing/lastStats","Tracing","Last Trace Stats");
        menuBar.add("/operator/tracing/traceGraph","Tracing","Trace Graph");
        menuBar.add("/operator/tracing/traceRoots","Tracing","Root Categories");
        menuBar.add("/operator/tracing/allCategories","Tracing","All Categories");
        menuBar.add("/operator/tracing/activeTraces","Tracing","Active Traces");
        menuBar.add("/operator/tracing/lastTraces","Tracing","Last Traces");
        menuBar.add("/operator/tracing/lastExceptions","Tracing","Last Exception Traces");

        menuBar.add("/operator/logging/status","Logging","Status");

        menuBar.add("/operator/httpServer/status/public","Servers","Public","Status");
        menuBar.add("/operator/httpServer/performance/public","Servers","Public","Performance");
        menuBar.add("/operator/httpServer/lastRequests/public","Servers","Public","Last Requests");
        menuBar.add("/operator/httpServer/lastNotFounds/public","Servers","Public","Last Not Founds (404s)");
        menuBar.add("/operator/httpServer/lastExceptionRequests/public","Servers","Public","Last Exception Requests");
        menuBar.add("/operator/httpServer/methods/public","Servers","Public","Methods");
        menuBar.add("/operator/httpServer/classDefinitions/public","Servers","Public","Class Definitions");

        menuBar.add("/operator/httpServer/status/private","Servers","Private","Status");
        menuBar.add("/operator/httpServer/performance/private","Servers","Private","Performance");
        menuBar.add("/operator/httpServer/lastRequests/private","Servers","Private","Last Requests");
        menuBar.add("/operator/httpServer/lastNotFounds/private","Servers","Private","Last Not Founds (404s)");
        menuBar.add("/operator/httpServer/lastExceptionRequests/private","Servers","Private","Last Exception Requests");
        menuBar.add("/operator/httpServer/methods/private","Servers","Private","Methods");
        menuBar.add("/operator/httpServer/classDefinitions/private","Servers","Private","Class Definitions");
        
        menuBar.add("/operator/httpServer/status/operator","Servers","Operator","Status");
        menuBar.add("/operator/httpServer/performance/operator","Servers","Operator","Performance");
        menuBar.add("/operator/httpServer/lastRequests/operator","Servers","Operator","Last Requests");
        menuBar.add("/operator/httpServer/lastNotFounds/operator","Servers","Operator","Last Not Founds (404s)");
        menuBar.add("/operator/httpServer/lastExceptionRequests/operator","Servers","Operator","Last Exception Requests");
        menuBar.add("/operator/httpServer/methods/operator","Servers","Operator","Methods");
        menuBar.add("/operator/httpServer/classDefinitions/operator","Servers","Operator","Class Definitions");

        serverApplication.getOperatorVariableManager().register(serverApplication.getTraceManager());
        serverApplication.getOperatorVariableManager().register(this);
    }

    
    @GET
    @Path("/operator/application/configuration")
    public Element configuration() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Configuration"); 
        DataTable table=page.content().returnAddInner(new OperatorTable(page.head()));
        
        table.setHeadRowItems("Name","Value","Description","Source","Source Context");
        for (ConfigurationItem item : this.serverApplication.getConfiguration().getConfigurationItemSnapshot())
        {
            table.addBodyRowItems(item.getName(),item.getValue(),item.getDescription(),item.getSource(),item.getSourceContext());
        }
        return page;
    }
    
    @GET
    @Path("/operator/application/futures")
    public Element futures() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Futures");
        DataTable table=page.content().returnAddInner(new OperatorTable(page.head()));

        table.setHeadRowItems("Category","Number","Duration (ms)","Executing","Ended");
        Future<?>[] array = this.serverApplication.getFutureScheduler().getFutureSnapshot();
        for (Future<?> item : array)
        {
            table.addBodyRowItems(item.getTrace().getCategory(),item.getTrace().getNumber()
                    ,Utils.nanosToDurationString(item.getTrace().getDurationNs()),item.getExecuting(),item.getCompleted());
        }
        return page;
    }

    @GET
    @Path("/operator/application/timers")
    public Element timers() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Timers");
        DataTable table=page.content().returnAddInner(new OperatorTable(page.head()));
        table.setHeadRow(new Row().add("Category")
                .addWithTitle("#", "Number")
                .add("Created","Status","Due","Countdown","Duration")
                .addWithTitle("Mode","Timer scheduling mode")
                .addWithTitle("Delay","Days hours:minutes:seconds.milliseconds")
                .addWithTitle("Period","Days hours:minutes:seconds.milliseconds")
                .addWithTitle("\u231B", "Number of attempts")
                .addWithTitle("\u2705", "Number of successful executions")
                .addWithTitle("\u274C", "Number of exceptions")
                .addWithTitle("\u26A0", "Number of misses")
                );


        long now = System.currentTimeMillis();
        TimerTask[] timerTasks = this.serverApplication.getTimerScheduler().getTimerTaskSnapshot();
        for (TimerTask timerTask : timerTasks)
        {
            table.addBodyRowItems(timerTask.getCategory()
                    ,timerTask.getNumber()
                    ,Utils.millisToLocalDateTimeString(timerTask.getCreated())
                    ,timerTask.getExecutableStatus()
                    ,Utils.millisToLocalDateTime(timerTask.getDue())
                    ,Utils.millisToDurationString(timerTask.getDue() - now)
                    ,Utils.nanosToDurationString(timerTask.getTotalDuration())
                    ,timerTask.getShedulingMode()
                    ,Utils.millisToDurationString(timerTask.getDelay())
                    ,Utils.millisToDurationString(timerTask.getPeriod())
                    ,timerTask.getAttempts()
                    ,timerTask.getSuccesses()
                    ,timerTask.getThrowables()
                    ,timerTask.getMisses()
                    );
        }
        return page;
    }
    @GET
    @Path("/operator/application/meters/all")
    public Element meterCategories(@DefaultValue("10") @QueryParam("interval") int interval) throws Throwable
    {
        MeterSnapshot snapshot=this.serverApplication.getMeterManager().getSnapshot();
        OperatorPage page=this.serverApplication.buildOperatorPage("Meters");

        form_post form=page.content().returnAddInner(new form_post());
        form.addInner("Sampling interval (seconds): ");
        SelectOptions options=form.returnAddInner(new SelectOptions());
        options.addOption(1,interval==1);
        options.addOption(2,interval==2);
        options.addOption(5,interval==5);
        options.addOption(10,interval==10);
        options.onchange("window.location='./all?interval='+(this.value);");
        
        page.content().addInner(new p());
        LevelMeterBox[] levelBoxes = snapshot.getLevelMeterBoxes();
        if (levelBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, true,"Level Meters"));
            DataTable table=accordion.content().returnAddInner(new OperatorTable(page.head()));
            table.setHeadRow(new Row().add("Category","Name","Level", "Highest","Highest TimeStamp","Description"));
        
            for (LevelMeterBox box : levelBoxes)
            {
                LevelMeter meter = box.getMeter();
                table.addBodyRowItems(box.getCategory(),box.getName(),meter.getLevel(),meter.getMaximumLevel()
                        ,Utils.millisToLocalDateTimeString(meter.getHighestLevelTimeStamp()),box.getDescription());
            }
        } 
        RateMeterBox[] rateBoxes = snapshot.getRateMeterBoxes();
        if (rateBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, true,"Rate Meters"));
            DataTable table=accordion.content().returnAddInner(new OperatorTable(page.head()));
            table.setHeadRow(new Row().add("Name","Rate","Count","Average","Description"));
            for (RateMeterBox box : rateBoxes)
            {
                RateMeter meter = box.getMeter();
                table.addBodyRowItems(box.getName(),String.format("%.4f", meter.sampleRate(interval))
                ,meter.getCount(),String.format("%.4f", meter.sampleRate(interval)),(box.getDescription()));
            }
        }
        CountMeterBox[] countBoxes = snapshot.getCountMeterBoxes();
        if (countBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, true,"Count Meters"));
            DataTable table=accordion.content().returnAddInner(new OperatorTable(page.head()));
            table.setHeadRow(new Row().add("Name","Count","Description"));
            for (CountMeterBox box : countBoxes)
            {
                CountMeter meter = box.getMeter();
                table.addBodyRowItems(box.getName(),meter.getCount(),box.getDescription());
            }
        }
        CountAverageRateMeterBox[] countAverageRateBoxes = snapshot.getCountAverageRateMeterBoxes();
        if (countAverageRateBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, true,"CountAverageRate Meters"));
            DataTable table=accordion.content().returnAddInner(new OperatorTable(page.head()));
            table.setHeadRow(new Row().add("Name","Average")
                    .addWithTitle("StdDev", "Standard Deviation")
                    .addWithTitle("Rate", "per second")
                    .add("Count","Total","Description"));
            for (CountAverageRateMeterBox box : countAverageRateBoxes)
            {
                CountAverageRateMeter meter = box.getMeter();
                AverageAndRate averageAndRate = meter.getCountAverageRate(interval);
                table.addBodyRowItems(box.getName(),String.format("%.4f", averageAndRate != null ? averageAndRate.getAverage() : "-")
                        ,String.format("%.4f", averageAndRate != null ? averageAndRate.getStandardDeviation() : "-")
                        ,String.format("%.4f", averageAndRate != null ? averageAndRate.getRate() : "-")
                        ,meter.getCount(),meter.getTotal(),box.getDescription());
            }
        }
        return page;
    }

    @GET
    @Path("/operator/application/meters/category")
    public Element meterCategory(@QueryParam("category") String category, @QueryParam("interval") @DefaultValue("10") long interval) throws Throwable
    {

        OperatorPage page=this.serverApplication.buildOperatorPage("Meter Category: "+category);
        form_post form=page.content().returnAddInner(new form_post());
        form.addInner("Sampling interval (seconds): ");
        SelectOptions options=form.returnAddInner(new SelectOptions());
        options.addOption(1,interval==1);
        options.addOption(2,interval==2);
        options.addOption(5,interval==5);
        options.addOption(10,interval==10);
        options.onchange("window.location='./all?interval='+(this.value);");
        page.content().addInner(new p());
        CategoryMeters categories = this.serverApplication.getMeterManager().getSnapshot().getMeterBoxes(category);
        LevelMeterBox[] levelBoxes = categories.getLevelMeterBoxes();
        if (levelBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, true,"Level Meters"));
            DataTable table=accordion.content().returnAddInner(new OperatorTable(page.head()));
            table.setHeadRow(new Row().add("Category","Name","Level", "Highest","Highest TimeStamp","Description"));
        
            for (LevelMeterBox box : levelBoxes)
            {
                LevelMeter meter = box.getMeter();
                table.addBodyRowItems(box.getCategory(),box.getName(),meter.getLevel(),meter.getMaximumLevel()
                        ,Utils.millisToLocalDateTimeString(meter.getHighestLevelTimeStamp()),box.getDescription());
            }
        }        
        RateMeterBox[] rateBoxes = categories.getRateMeterBoxes();
        if (rateBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, true,"Rate Meters"));
            accordion.button().addInner("Rate Meters");
            DataTable table=accordion.content().returnAddInner(new OperatorTable(page.head()));
            table.setHeadRow(new Row().add("Name","Rate","Count","Average","Description"));
            for (RateMeterBox box : rateBoxes)
            {
                RateMeter meter = box.getMeter();
                table.addBodyRowItems(box.getName(),String.format("%.4f", meter.sampleRate(interval))
                ,meter.getCount(),String.format("%.4f", meter.sampleRate(interval)),(box.getDescription()));
            }
        }

        CountMeterBox[] countBoxes = categories.getCountMeterBoxes();
        if (countBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null,true,"Count Meters"));
            DataTable table=accordion.content().returnAddInner(new OperatorTable(page.head()));
            table.setHeadRow(new Row().add("Name","Count","Description"));
            for (CountMeterBox box : countBoxes)
            {
                CountMeter meter = box.getMeter();
                table.addBodyRowItems(box.getName(),meter.getCount(),box.getDescription());
            }
        }
        CountAverageRateMeterBox[] countAverageRateBoxes = categories.getCountAverageMeterBoxes();
        if (countAverageRateBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, true,"CountAverageRate Meters"));
            DataTable table=accordion.content().returnAddInner(new OperatorTable(page.head()));
            table.setHeadRow(new Row().add("Name","Average")
                    .addWithTitle("StdDev", "Standard Deviation")
                    .addWithTitle("Rate", "per second")
                    .add("Count","Total","Description"));
            for (CountAverageRateMeterBox box : countAverageRateBoxes)
            {
                CountAverageRateMeter meter = box.getMeter();
                AverageAndRate averageAndRate = meter.getCountAverageRate(interval);
                table.addBodyRowItems(box.getName(),String.format("%.4f", averageAndRate != null ? averageAndRate.getAverage() : "-")
                        ,String.format("%.4f", averageAndRate != null ? averageAndRate.getStandardDeviation() : "-")
                        ,String.format("%.4f", averageAndRate != null ? averageAndRate.getRate() : "-")
                        ,meter.getCount(),meter.getTotal(),box.getDescription());
            }
        }
        return page;
    }

    @GET
    @Path("/operator/application/meters/categories")
    public Element meterCategories() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Meter Categories");
        Panel panel=page.content().returnAddInner(new Level1Panel(page.head(),"Categories"));
        String[] categories = this.serverApplication.getMeterManager().getSnapshot().getCategories();
        VerticalMenu menu=panel.content().returnAddInner(new VerticalMenu(page.head(), "menu"));
        for (String category:categories)
        {
            menu.addMenuItem(category, new PathAndQueryBuilder("/operator/application/meters/category").addQuery("category", category).toString());
        }
        return page;
    }
    

    void write(Table table, Trace trace, Object family) throws Exception
    {
        table.addBodyRow(new Row()
                .add(family,trace.getCategory()
                ,trace.getNumber()
                ,Utils.millisToLocalDateTime(trace.getCreated())
                ,trace.getActiveNs() / 1000000
                ,trace.getWaitNs() / 1000000
                ,trace.getDurationNs() / 1000000
                ,trace.isWaiting()
                ,trace.getDetails()
                ,trace.getThread().getName()
                ).onclick("window.location='"+new PathAndQueryBuilder("./activeTrace").addQuery("number", trace.getNumber()).toString()+"'")
                );

    }

    @GET
    @Path("/operator/tracing/activeTrace")
    public Element activeTrace(@QueryParam("number") long number) throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Active Trace");
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
            
            WideTable table=page.content().returnAddInner(new WideTable(page.head()));
            table.setHeadRow(new Row()
                    .addWithTitle("T", "*=target trace, A=ancestor, C:n=Child")
                    .add("Category","Number","Created")
                    .addWithTitle("Active", "(ms)")
                    .addWithTitle("Wait", "(ms)")
                    .addWithTitle("Duration", "Active+Wait (ms)")
                    .add("Waiting","Details","thread"));

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
                    write(table, trace, "C:"+index);
                }
                index++;
            }
            Trace trace = found;
            write(table, trace, "*");
            for (trace = found.getParent(); trace != null; trace = trace.getParent())
            {
                write(table, trace, "A");
            }
            Accordion accordion=new Accordion(page.head(), null, true, "Stack Trace");
            StackTraceElement[] stackTrace = found.getThread().getStackTrace();
            accordion.content().addInner(toString(stackTrace, 0));

        }
        else
        {
            page.content().addInner("trace ended");
        }
        return page;
    }

    @GET
    @Path("/operator/tracing/activeStats")
    public Element activeTraceStats() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Active Trace Stats");
        Trace[] traces = this.serverApplication.getTraceManager().getActiveSnapshot();
        DataTable table=page.content().returnAddInner(new OperatorTable(page.head()));
        table.setHeadRow(new Row().add("Category","Number").addWithTitle("Active", "milliseconds").addWithTitle("Wait", "milliseconds").addWithTitle("Duration", "milliseconds").add("Waiting","Details","Created"));
        for (Trace trace : traces)
        {
            Row row=new Row()
                    .addWithUrl(trace.getCategory(),new PathAndQueryBuilder("./activeTrace").addQuery("number", trace.getNumber()).toString(),true)
                    .add(trace.getNumber(),trace.getActiveNs() / 1000000,trace.getWaitNs() / 1000000,trace.getDurationNs() / 1000000,trace.isWaiting(),trace.getDetails(),Utils.millisToLocalDateTime(trace.getCreated()));
            table.addBodyRow(row);
        }
        return page;
    }

    @GET
    @Path("/operator/tracing/lastStats")
    public Element lastStats() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Active Trace Stats");
        TraceStats[] array = this.serverApplication.getTraceManager().getStatsSnapshotAndReset();
        DataTable table=page.content().returnAddInner(new OperatorTable(page.head()));

        table.setHeadRow(new Row().add("Category","Count")
                .addWithTitle("Average", "milliseconds")
                .addWithTitle("Stddev", "Standard deviation in milliseconds")
                .addWithTitle("Total", "Total duration in milliseconds"));
        for (TraceStats item : array)
        {
            CountAverageRateMeter meter = item.getMeter();
            AverageAndRate ar = meter.getMarkCountAverage(this.rateSamplingDuration);
            Row row=new Row()
                    .addWithUrl(item.getCategory(),new PathAndQueryBuilder("./trace").addQuery("category", item.getCategory()).toString(),true)
                    .add(meter.getCount()
                    ,nanoToDefaultFormat(ar.getAverage())
                    ,nanoToDefaultFormat(ar.getStandardDeviation())
                    ,nanoToDefaultFormat(meter.getTotal()));

            table.addBodyRow(row);
        }
        return page;
    }

    private static DecimalFormat millisecondFormat = new DecimalFormat("#.###");

    private String nanoToDefaultFormat(double value)
    {
        return millisecondFormat.format(value / 1.0e6);
    }

    @GET
    @Path("/operator/tracing/traceGraph")
    public Element traceGraph() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Trace Graph");
        Map<String, TraceNode> map = this.serverApplication.getTraceManager().getTraceRootSnapshot();
        Table table=page.content().returnAddInner(new Table());
        table.style("border-collapse:collapse;");
        for (Entry<String, TraceNode> entry : map.entrySet())
        {
            tr tr=new tr();
            table.addBodyRow(tr);
            writeTraceGraphNode(page.head(),tr,entry,0);
        }
        return page;
    }

    @GET
    @Path("/operator/tracing/traceRoots")
    public Element traceRoots() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Trace Roots");
        Map<String, TraceNode> map = this.serverApplication.getTraceManager().getTraceRootSnapshot();
        DataTable table=page.content().returnAddInner(new OperatorTable(page.head()));
        table.setHeadRow(new Row().add("Category","Count")
                .addWithTitle("Average", "Milliseconds")
                .addWithTitle("Duration", "Milliseconds")
                .addWithTitle("Wait", "Milliseconds")
                );

        for (Entry<String, TraceNode> entry : map.entrySet())
        {
            TraceNode node = entry.getValue();
            Row row=new Row()
                    .addWithUrl(entry.getKey(),new PathAndQueryBuilder("./trace").addQuery("category", entry.getKey()).toString(),true)
                    .add(node.getCount()
                    ,nanoToDefaultFormat(node.getTotalDurationNs() / node.getCount())
                    ,nanoToDefaultFormat(node.getTotalDurationNs())
                    ,nanoToDefaultFormat(node.getTotalWaitNs()));
            table.addBodyRow(row);
        }
        return page;
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
      public Element trace(@QueryParam("category") String category) throws Throwable
      {
          OperatorPage page=this.serverApplication.buildOperatorPage("Trace Category");
          page.content().addInner("Subtree of category and path to root");
          Table table=page.content().returnAddInner(new Table());
          table.style("border-collapse:collapse;");
          Map<String, TraceNode> map = this.serverApplication.getTraceManager().getTraceRootSnapshot();
          for (Entry<String, TraceNode> entry : map.entrySet())
          {
              if (isChild(category, entry))
              {
                  tr tr=new tr();
                  table.addBodyRow(tr);
                  writeTraceGraphNodeToCategory(page.head(),tr, category, entry, 0);
              }
          }
          return page;
      }

      private String getTraceBoxStyle()
      {
          return "border-style:solid;border-color:#888;border-width:1px 0 1px 0;";
      }
      private void writeTraceGraphNodeToCategory(Head head,tr tr, String category,Entry<String, TraceNode> entry, int level) throws Exception
      {
          if (entry.getKey().equals(category))
          {
              writeTraceGraphNode(head,tr, entry, level + 1);
              return;
          }
          TraceNode node = entry.getValue();
          td td=tr.returnAddInner(new td());
          td.style(this.getTraceBoxStyle());
          
          Panel panel=td.returnAddInner(new Level2Panel(head,null));
          panel.heading().addInner(new a().href(new PathAndQueryBuilder("./trace").addQuery("category", entry.getKey()).toString()).addInner(entry.getKey()));

          writeNode(panel,node);
          
          if (node.getChildTraces() != null)
          {
              td=tr.returnAddInner(new td());
              td.style(this.getTraceBoxStyle());
              Table table=td.returnAddInner(new Table());
              table.style("border-collapse:collapse;");
              for (Entry<String, TraceNode> child : node.getChildTraces().entrySet())
              {
                  if (isChild(category, child))
                  {
                      tr=new tr();
                      table.addBodyRow(tr);
                      writeTraceGraphNodeToCategory(head,tr, category, child, level + 1);
                  }
              }
          }
      }

      private void writeNode(Panel panel,TraceNode node)
      {
          NameValueList list=panel.content().returnAddInner(new NameValueList());
          list.add("Count", node.getCount());
          list.add("Average", nanoToDefaultFormat(node.getTotalDurationNs() / node.getCount()));  //Milliseconds
          list.add("Duration", nanoToDefaultFormat(node.getTotalDurationNs()));
          list.add("Wait", nanoToDefaultFormat(node.getTotalWaitNs()));
      }
      
      private void writeTraceGraphNode(Head head,tr tr, Entry<String, TraceNode> entry, int level) throws Exception
      {
          TraceNode node = entry.getValue();
          td td=tr.returnAddInner(new td());
          td.style(this.getTraceBoxStyle());
          Panel panel=td.returnAddInner(new Level2Panel(head,null));
          panel.heading().addInner(new a().href(new PathAndQueryBuilder("./trace").addQuery("category", entry.getKey()).toString()).addInner(entry.getKey()));

          writeNode(panel,node);

          if (node.getChildTraces() != null)
          {
              td=tr.returnAddInner(new td());
              td.style(this.getTraceBoxStyle());

              Table table=td.returnAddInner(new Table());
              table.style("border-collapse:collapse;");
              for (Entry<String, TraceNode> child : node.getChildTraces().entrySet())
              {
                  tr=new tr();
                  table.addBodyRow(tr);
                  writeTraceGraphNode(head, tr, child, level + 1);
              }
          }
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
    public Element traceAllCategories() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("All Trace Categories");
        DataTable table=page.content().returnAddInner(new OperatorTable(page.head()));
        Map<String, TraceNode> map = this.serverApplication.getTraceManager().getTraceRootSnapshot();
        HashMap<String, ArrayList<TraceNode>> all = new HashMap<>();
        for (Entry<String, TraceNode> entry : map.entrySet())
        {
            addToAll(all, entry);
        }
        table.setHeadRow(new Row().add("Category","Count")
                .addWithTitle("Total", "Total duration in seconds")
                .addWithTitle("Wait", "Total wait duration in seconds")
                .addWithTitle("occurs", "The number of times the category occurs in the trace graph")
                .add("root"));

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

            Row row=new Row()
                    .add(entry.getKey(),new PathAndQueryBuilder("./trace").addQuery("category", entry.getKey()).toString(),true)
                    .add(count,nanoToDefaultFormat(totalDurationNs),nanoToDefaultFormat(totalWaitNs),list.size(),map.containsKey(entry.getKey()));
            table.addBodyRow(row);

        }
        return page;
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


    @GET
    @Path("/operator/tracing/lastTraces")
    public Element lastTraces() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Last Traces");
        writeTraces(page, this.serverApplication.getTraceManager().getLastTraces());
        return page;
    }

    @GET
    @Path("/operator/tracing/lastExceptions")
    public Element lastExeptions() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Last Exception Traces");
        writeTraces(page, this.serverApplication.getTraceManager().getLastExceptionTraces());
        return page;
    }

    @GET
    @Path("/operator/tracing/activeTraces")
    public Element activeTraces() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Active Traces");
        writeTraces(page, this.serverApplication.getTraceManager().getActiveSnapshot());
        return page;

    }
    private void writeTraces(OperatorPage page, Trace[] traces)
    {
        page.content().addInner(new p().addInner("Count: " + traces.length));
        for (int i = traces.length - 1; i >= 0; i--)
        {
            Trace trace = traces[i];
            Panel panel=page.content().returnAddInner(new Level2Panel(page.head(), trace.getCategory()));
            writeTrace(page.head(),panel,trace);
        }
    }



    @GET
    @Path("/operator/httpServer/performance/{server}")
    public Element performance(@PathParam("server")String server) throws Throwable
    {
        OperatorPage page=buildServerOperatorPage("Performance: ",server);
        HttpServer httpServer=getHttpServer(server);
        RateMeter requestRateMeter = httpServer.getRequestRateMeter();
        double requestRate = requestRateMeter.sampleRate(this.rateSamplingDuration);
        WideTable infoTable=page.content().returnAddInner(new WideTable(page.head()));
        infoTable.setHeadRowItems("Request Rate","Total Requests");
        infoTable.addBodyRowItems(DOUBLE_FORMAT.format(requestRate),requestRateMeter.getCount());
        
        page.content().addInner(new p());
        Panel requestHandlerPanel=page.content().returnAddInner(new Level1Panel(page.head(),"RequestHandlers"));
        DataTable table=requestHandlerPanel.returnAddInner(new OperatorTable(page.head()));
        Row row=new Row();
        row.add("Method")
            .addWithTitle("Count", "Count of total requests")
            .addWithTitle("% Count", "Count percentage")
            .addWithTitle("Duration", "Total duration in method in days hours:minutes:seconds.milliseconds")
            .addWithTitle("% Dur", "Percentage Duration")
            .addWithTitle("Ave Dur", "Average duration of request in milliseconds")
            .addWithTitle("Rate", "Request rate per second")
            .addWithTitle("ReqSize", "Average uncompressed size of request content in bytes")
            .addWithTitle("RespSize", "Average uncompressed size of response content in bytes");
        table.setHeadRow(row);

        double totalAll = 0;
        long totalCount = 0;
        RequestHandler[] requestHandlers = httpServer.getRequestHandlers();
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

            row=new Row();
            row.addWithUrl(requestHandler.getHttpMethod() + " " + requestHandler.getPath(),new PathAndQueryBuilder("/operator/httpServer/info").addQuery("key", requestHandler.getKey()).addQuery("server", server).toString(),true)
            .add(count)
            .add(DOUBLE_FORMAT.format(countPercentage))
            .add(Utils.millisToDurationString((long) totalMilliseconds))
            .add(DOUBLE_FORMAT.format(totalPercentage))
            .add(count > 0 ? DOUBLE_FORMAT.format(total / (1.0e6 * count)) : 0)
            .add(DOUBLE_FORMAT.format(rate))
            .add(count > 0 ? requestHandler.getRequestUncompressedContentSizeMeter().getTotal() / count : 0)
            .add(count > 0 ? requestHandler.getResponseUncompressedContentSizeMeter().getTotal() / count : 0);
            table.addBodyRow(row);
            
        }
        return page;
    }


    private void writeTrace(Head head,Panel panel,Trace trace)
    {
        WideTable table=panel.content().returnAddInner(new WideTable(head));
        Row headRow=new Row()
            .add("Created")
            .addWithTitle("Number", "Sequence number")
            .addWithTitle("Parent", "Parent sequence number if trace has parent")
            .addWithTitle("Duration", "Milliseconds")
            .addWithTitle("Wait", "Milliseconds")
            .add("Waiting","Closed","Thread","ThreadId")
            ;
        table.setHeadRow(headRow);
        Row row=new Row()
                .add(Utils.millisToLocalDateTimeString(trace.getCreated())
                ,trace.getNumber()
                ,trace.getParent() == null ? "" : trace.getParent().getNumber()
                ,nanoToDefaultFormat(trace.getActiveNs())
                ,nanoToDefaultFormat(trace.getWaitNs())
                ,trace.isWaiting()
                ,trace.isClosed()
                ,trace.getThread().getName()
                ,trace.getThread().getId());
        table.addBodyRow(row);

        NameValueList list=null;
        String details = trace.getDetails();
        if (details != null)
        {
            if (list==null)
            {
                list=new NameValueList();
            }
            list.add("Details",details);
        }

        String fromLink = trace.getFromLink();
        if (fromLink != null)
        {
            if (list==null)
            {
                list=new NameValueList();
            }
            list.add("FromLink",fromLink);
        }

        String toLink = trace.getToLink();
        if (toLink != null)
        {
            if (list==null)
            {
                list=new NameValueList();
            }
            list.add("ToLink",toLink);
        }
        if (list!=null)
        {
            Panel listPanel=panel.content().returnAddInner(new Level3Panel(head,"Additional Trace Info"));
            listPanel.content().addInner(list);
        }

        panel.content().addInner(new p());
        if (trace.getThrowable() != null)
        {
            Accordion accordion=panel.content().returnAddInner(new Accordion(head, null, true, "Exception: "+trace.getThrowable().getMessage()));
            accordion.content().addInner(Utils.toString(trace.getThrowable()));
            panel.content().addInner(new p());
        }
        StackTraceElement[] currentStackTrace = trace.getThread().getStackTrace();
        if (currentStackTrace != null)
        {
            Accordion accordion=panel.content().returnAddInner(new Accordion(head, null, false, "Current Stack Trace"));
            accordion.content().addInner(toString(currentStackTrace, 0));
        }
        StackTraceElement[] createStackTrace = trace.getCreateStackTrace();
        if (createStackTrace != null)
        {
            Accordion accordion=panel.content().returnAddInner(new Accordion(head, null, false, "Create Stack Trace"));
            accordion.content().addInner(toString(createStackTrace, 0));
        }
        StackTraceElement[] closeStackTrace = trace.getCloseStackTrace();
        if (closeStackTrace != null)
        {
            Accordion accordion=panel.content().returnAddInner(new Accordion(head, null, false, "Close Stack Trace"));
            accordion.content().addInner(toString(closeStackTrace, 0));
        }
    }
    
    private void writeHeaders(Head head,Panel panel,String headers)
    {
        if (headers==null)
        {
            return;
        }
        panel.content().addInner(new p());
        String[] array=Utils.split(headers, '\r');
        Accordion accordion=panel.content().returnAddInner(new Accordion(head,null,false, "Headers (Count: "+(array.length-1)+")"));
        NameValueList list=accordion.content().returnAddInner(new NameValueList());
        for (String item:array)
        {
            int index=item.indexOf(':');
            if (index<0)
            {
                continue;
            }
            String name=item.substring(0, index);
            String value=item.substring(index+1);
            list.add(name,value);
        }
    }    
    
    private void writeContent(Head head,Panel panel,String text)
    {
        if (text==null)
        {
            return;
        }
        panel.content().addInner(new p());
//        Panel textpanel=panel.content().returnAddInner(new Level3Panel(head,"Request Content (Length: "+text.length()+")"));
        Accordion textAccodion=panel.content().returnAddInner(new Accordion(head,false,"Request Content (Length: "+text.length()+")"));
        textAccodion.content().addInner(new textarea().readonly().style("width:100%").addInner(text));
    }
    
    private void writeRequest(OperatorPage page,RequestLogEntry entry)
    {
        Head head=page.head();
        Panel panel=page.content().returnAddInner(new Level1Panel(page.head(),entry.getRequestHandler().getKey()));
        Level2Panel tracePanel=panel.content().returnAddInner(new Level2Panel(head,"Trace"));
        writeTrace(head,tracePanel,entry.getTrace());
        
        panel.content().addInner(new p());
        Panel requestPanel=panel.content().returnAddInner(new Level2Panel(head,"Request"));
        WideTable table=requestPanel.content().returnAddInner(new WideTable(head));
        table.setHeadRow(new Row().add("Request","Query","Remote"));
        table.addBodyRow(new Row().add(entry.getRequest(),entry.getQueryString(),entry.getRemoteEndPoint()));
        writeHeaders(head,requestPanel,entry.getRequestHeaders());
        writeContent(head,requestPanel,entry.getRequestContentText());

        panel.content().addInner(new p());
        Panel responsePanel=panel.content().returnAddInner(new Level2Panel(head,"Response - Status Code: "+entry.getStatusCode()));
        writeHeaders(head,responsePanel,entry.getResponseHeaders());
        writeContent(head, responsePanel, entry.getResponseContentText());
    }    
    
    private void writeRequestLogEntries(OperatorPage page, RequestLogEntry[] entries) throws Exception
    {
        for (RequestLogEntry entry : entries)
        {
            writeRequest(page, entry);
            page.content().addInner(new p());
        }
    }

    @GET
    @Path("/operator/httpServer/lastRequests/{server}")
    public Element lastRequests(@PathParam("server") String server) throws Throwable
    {
        HttpServer httpServer=this.getHttpServer(server);
        OperatorPage page=buildServerOperatorPage("Last Requests", server);
        RequestLogEntry[] entries = httpServer.getLastRequestLogEntries();
        writeRequestLogEntries(page, entries);
        return page;
    }

    @GET
    @Path("/operator/httpServer/lastExceptionRequests/{server}")
    public Element lastExceptionRequests(@PathParam("server") String server) throws Throwable
    {
        HttpServer httpServer=this.getHttpServer(server);
        OperatorPage page=buildServerOperatorPage("Last Exception Requests", server);
        RequestLogEntry[] entries = httpServer.getLastExceptionRequestLogEntries();
        writeRequestLogEntries(page, entries);
        return page;
    }


    @GET
    @Path("/operator/httpServer/lastNotFounds/{server}")
    public Element lastNotFoundRequests(@PathParam("server") String server) throws Throwable
    {
        HttpServer httpServer=this.getHttpServer(server);
        OperatorPage page=buildServerOperatorPage("Last Not Founds (404s)",server);
        
        RequestHandlerNotFoundLogEntry[] entries = httpServer.getRequestHandlerNotFoundLogEntries();
        for (RequestHandlerNotFoundLogEntry entry : entries)
        {
            Trace trace = entry.getTrace();
            String title= entry.getMethod() + " " + entry.getURI();
            Panel panel=page.content().returnAddInner(new Level1Panel(page.head(),title));
            writeTrace(page.head(),panel,trace);

            panel.content().addInner(new p());
            WideTable table=panel.content().returnAddInner(new WideTable(page.head()));
            table.setHeadRow(new Row().add("Query","Remote"));
            table.addBodyRow(new Row().add(entry.getQueryString(),entry.getRemoteEndPoint()));
            writeHeaders(page.head(),panel,entry.getRequestHeaders());

            page.content().addInner(new p());
        }
        return page;
    }

    @GET
    @Path("/operator/httpServer/status/{server}")
    public Element status(@PathParam("server") String server) throws Throwable
    {
        HttpServer httpServer=getHttpServer(server);
        RequestHandler[] requestHandlers = httpServer.getRequestHandlers();
        OperatorPage page=buildServerOperatorPage("Server Status",server);
        DataTable table=page.content().returnAddInner(new OperatorTable(page.head()));
        Row row=new Row();
        row.add("Method","Path","200","300","400","500");
        table.setHeadRow(row);
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
            row=new Row();
            row.add(requestHandler.getHttpMethod())
            .addWithUrl(requestHandler.getPath(),new PathAndQueryBuilder("/operator/httpServer/info").addQuery("key", requestHandler.getKey()).addQuery("server", server).toString(),true)
            .add(statusCodes.get(200)
                ,statusCodes.get(300)
                ,statusCodes.get(400)
                ,statusCodes.get(500)
            );
            table.addBodyRow(row);
        }
        return page;
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

    
    private HttpServer getHttpServer(String server) throws Exception
    {
        if ("public".equals(server))
        {
            return this.serverApplication.getPublicServer();
        }
        else if ("private".equals(server))
        {
            return this.serverApplication.getPrivateServer();
        }
        else if ("operator".equals(server))
        {
            return this.serverApplication.getOperatorServer();
        }
        throw new Exception();
    }
    
    private OperatorPage buildServerOperatorPage(String title,String server) throws Throwable
    {
        HttpServer httpServer=getHttpServer(server);
        OperatorPage page=this.serverApplication.buildOperatorPage(title);
        int[] ports=httpServer.getPorts();
        if (ports.length==1)
        {
            page.content().addInner("Server: "+server+", port: "+ports[0]);
        }
        else
        {
            page.content().addInner("Server: "+server+", ports: "+Utils.combine(Utils.intArrayToList(ports),","));
        }
        page.content().addInner(new hr());
        return page;
    }
    
    @GET
    @Path("/operator/httpServer/methods/{server}")
    public Element methods(@PathParam("server") String server) throws Throwable
    {
        HttpServer httpServer=getHttpServer(server);
        OperatorPage page=buildServerOperatorPage("Methods",server);
        RequestHandler[] requestHandlers = httpServer.getRequestHandlers();
        DataTable table=page.content().returnAddInner(new OperatorTable(page.head()));
        table.setHeadRow(new Row().add("Method","Path","Description","Filters"));
        for (RequestHandler requestHandler : requestHandlers)
        {
            Row row=new Row().add(requestHandler.getHttpMethod());
            row.addWithUrl(requestHandler.getPath(), new PathAndQueryBuilder("/operator/httpServer/method/"+server).addQuery("key", requestHandler.getKey()).toString(),true);
            
            Method method = requestHandler.getMethod();
            Description description = method.getAnnotation(Description.class);
            if (description != null)
            {
                row.add(description.value());
            }
            else
            {
                row.add("");
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
                row.add(sb);
            }
            else
            {
                row.add("");
            }
            table.addBodyRow(row);
        }
        return page;
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

    private void writeParameterInfos(Head head,Panel panel, String heading, RequestHandler handler, ParameterSource filter)
    {
        if (Arrays.stream(handler.getParameterInfos()).filter(info ->
        {
            return info.getSource() == filter;
        }).count() == 0)
        {
            return;
        }

        Method method = handler.getMethod();
        Level2Panel panel2=panel.content().returnAddInner(new Level2Panel(head, heading));
        
        WideTable table=panel2.content().returnAddInner(new WideTable(head));
        table.setHeadRow(new Row().add("Name","Type","Description","Default"));
        for (ParameterInfo info : handler.getParameterInfos())
        {
            if (info.getSource() == filter)
            {
                Parameter parameter = method.getParameters()[info.getIndex()];
                table.addBodyRow(new Row().add(info.getName(),parameter.getType().getName(),getDescription(parameter),info.getDefaultValue()));
            }
        }
        panel.content().addInner(new p());
    }

    private void writeInputParameterInfos(Head head,InnerElement<?> container, AjaxButton button, String heading, RequestHandler handler, ParameterSource source)
    {
        if (Arrays.stream(handler.getParameterInfos()).filter(info ->
        {
            return info.getSource() == source;
        }).count() == 0)
        {
            return;
        }

        Method method = handler.getMethod();
        fieldset field=container.returnAddInner(new fieldset());
        field.addInner(new legend().addInner(heading));
        WideTable table=field.returnAddInner(new WideTable(head));
        table.setHeadRow(new Row().add("Name","Type","Description","Default","Value"));
        for (ParameterInfo info : handler.getParameterInfos())
        {
            if (info.getSource() == source)
            {
                String name = info.getName();
                Parameter parameter = method.getParameters()[info.getIndex()];
                Row row=new Row().add(info.getName(),parameter.getType().getName(),getDescription(parameter),info.getDefaultValue());
                table.addBodyRow(row);
                String key = source.toString() + name;
                if (parameter.getType() == boolean.class)
                {
                    button.val(key, key);
                    row.add(new input_checkbox().id(key).name(key).checked((boolean)info.getDefaultValue()));
                }
                else
                {
                    button.val(key, key);
                    row.add(new input_text().id(key).name(key).style("background-color:#ffa;width:100%;").placeholder(info.getDefaultValue() == null ? "" : info.getDefaultValue().toString()));
                }
            }
        }
    }

    private ParameterInfo findContentParameter(RequestHandler requestHandler)
    {
        for (ParameterInfo info : requestHandler.getParameterInfos())
        {
            if (info.getSource() == ParameterSource.CONTENT)
            {
//                Method method = requestHandler.getMethod();
                return info;
            }
        }
        return null;
    }

    static public class ParameterWriter
    {
        final ArrayList<Class<?>> usedClasses = new ArrayList<>();
        final HashSet<String> shownClasses = new HashSet<>();
        final Panel parentPanel;
        final Head head;

        ParameterWriter(Head head,Panel parentPanel)
        {
            this.parentPanel=parentPanel;
            this.head=head;
        }

        void write(Class<?> type)
        {
            if (this.shownClasses.contains(type.getName()))
            {
                return;
            }
            this.shownClasses.add(type.getName());
            Level3Panel panel=this.parentPanel.content().returnAddInner(new Level3Panel(this.head,"Class: "+type.getName()));
            Description description = type.getAnnotation(Description.class);
            if (description != null)
            {
                panel.content().addInner(description.value());
            }
            WideTable table=panel.content().returnAddInner(new WideTable(head));
            table.setHeadRow(new Row().add("Type","Name","Description"));
            for (Field field : type.getDeclaredFields())
            {
                Row row=new Row();
                table.addBodyRow(row);
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

                if (fieldType.isArray())
                {
                    row.add(Utils.escapeHtml(fieldType.getComponentType().getName() + "[]"));
                    fieldType = fieldType.getComponentType();
                }
                else
                {
                    row.add(Utils.escapeHtml(fieldType.getName()));
                }
                row.add(field.getName());
                description = field.getAnnotation(Description.class);
                if (description != null)
                {
                    row.add(description.value());
                }
                else
                {
                    row.add("");
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
            this.parentPanel.content().addInner(new p());

            if (this.usedClasses.size() > 0)
            {
                Class<?> next = this.usedClasses.remove(0);
                write(next);
            }
        }
    }

    @GET
    @Path("/operator/httpServer/info")
    public Element info(@QueryParam("key") String key,@QueryParam("server") String server) throws Throwable
    {
        OperatorPage page=buildServerOperatorPage("Method: "+key, server);
        HttpServer httpServer=getHttpServer(server);
        RequestHandler requestHandler = httpServer.getRequestHandler(key);

        Map<Integer, CountAverageRateMeter> meters = requestHandler.getStatusMeters();
        if (meters.size()>0)
        {
            Panel durationPanel=page.content().returnAddInner(new Level1Panel(page.head(),"Durations"));
            WideTable table=durationPanel.content().returnAddInner(new WideTable(page.head()));
                table.setHeadRow(new Row()
                        .addWithTitle("Code", "Status Code")
                        .add("Count")
                        .addWithTitle("Rate", "per second")
                        .addWithTitle("Average", "Milliseconds")
                        .addWithTitle("StdDev", "Standard Deviation")
                        .addWithTitle("Total","Total duration in milliseconds")
                        );
            for (Entry<Integer, CountAverageRateMeter> entry : meters.entrySet())
            {
                CountAverageRateMeter meter = entry.getValue();
                AverageAndRate averageAndRate = meter.getCountAverageRate(this.rateSamplingDuration);
                if (averageAndRate != null)
                {
                    table.addBodyRow(new Row().add(
                            entry.getKey()
                            ,meter.getCount()
                            ,DOUBLE_FORMAT.format(averageAndRate.getRate())
                            ,DOUBLE_FORMAT.format(averageAndRate.getAverage() / 1.0e6)
                            ,DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation() / 1.0e6)
                            ,DOUBLE_FORMAT.format(meter.getTotal() / 1.0e6)
                            ));
                }
                else
                {
                    table.addBodyRow(new Row().add(
                            entry.getKey()
                            ,meter.getCount()
                            ,""
                            ,""
                            ,""
                            ,""
                            ));
    
                }
            }
        }
        {
            long requestUncompressed = 0;
            long requestCompressed = 0;
            long responseUncompressed = 0;
            long responseCompressed = 0;
    
            page.content().addInner(new p());
            Panel panel=page.content().returnAddInner(new Level1Panel(page.head(), "Content Sizes"));
            WideTable table=panel.content().returnAddInner(new WideTable(page.head()));
            table.setHeadRow(new Row().add("")
                    .addWithTitle("Average", "Average bytes")
                    .addWithTitle("StDev", "Standard Deviation")
                    .add("Total Bytes","KB","MB","GB","TB"));
            
            CountAverageRateMeter requestMeter = requestHandler.getRequestUncompressedContentSizeMeter();
            AverageAndRate averageAndRate = requestMeter.getCountAverageRate(this.rateSamplingDuration);
            if (averageAndRate != null)
            {
                long total = requestUncompressed = requestMeter.getTotal();
                table.addBodyRow(new Row().add(
                        "Request uncompressed content"
                        ,DOUBLE_FORMAT.format(averageAndRate.getAverage())
                        ,DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation())
                        ,total
                        ,total / 1024
                        ,total / 1024 / 1024
                        ,total / 1024 / 1024 / 1024
                        ,total / 1024 / 1024 / 1024 / 1024));
            }
            else
            {
                long total = requestUncompressed = requestMeter.getTotal();
                table.addBodyRow(new Row().add(
                        "Request uncompressed content"
                        ,""
                        ,""
                        ,total
                        ,total / 1024
                        ,total / 1024 / 1024
                        ,total / 1024 / 1024 / 1024
                        ,total / 1024 / 1024 / 1024 / 1024));
            }
    
            CountAverageRateMeter compressedRequestMeter = requestHandler.getRequestCompressedContentSizeMeter();
            averageAndRate = compressedRequestMeter.getCountAverageRate(this.rateSamplingDuration);
            if (averageAndRate != null)
            {
                long total = requestCompressed = compressedRequestMeter.getTotal();
                table.addBodyRow(new Row().add(
                        "Request content"
                        ,DOUBLE_FORMAT.format(averageAndRate.getAverage())
                        ,DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation())
                        ,total
                        ,total / 1024
                        ,total / 1024 / 1024
                        ,total / 1024 / 1024 / 1024
                        ,total / 1024 / 1024 / 1024 / 1024));
            }
            else
            {
                long total = requestCompressed = compressedRequestMeter.getTotal();
                table.addBodyRow(new Row().add(
                        "Request content"
                        ,""
                        ,""
                        ,total
                        ,total / 1024
                        ,total / 1024 / 1024
                        ,total / 1024 / 1024 / 1024
                        ,total / 1024 / 1024 / 1024 / 1024));
            }
    
            CountAverageRateMeter responseMeter = requestHandler.getResponseUncompressedContentSizeMeter();
            averageAndRate = responseMeter.getCountAverageRate(this.rateSamplingDuration);
            if (averageAndRate != null)
            {
                long total = responseUncompressed = responseMeter.getTotal();
                table.addBodyRow(new Row().add(
                        "Response uncompressed content"
                        ,DOUBLE_FORMAT.format(averageAndRate.getAverage())
                        ,DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation())
                        ,total
                        ,total / 1024
                        ,total / 1024 / 1024
                        ,total / 1024 / 1024 / 1024
                        ,total / 1024 / 1024 / 1024 / 1024));
            }
            else
            {
                long total = responseUncompressed = responseMeter.getTotal();
                table.addBodyRow(new Row().add(
                        "Response uncompressed content"
                        ,""
                        ,""
                        ,total
                        ,total / 1024
                        ,total / 1024 / 1024
                        ,total / 1024 / 1024 / 1024
                        ,total / 1024 / 1024 / 1024 / 1024));
            }
    
            CountAverageRateMeter compressedResponseMeter = requestHandler.getResponseCompressedContentSizeMeter();
            averageAndRate = compressedResponseMeter.getCountAverageRate(this.rateSamplingDuration);
            if (averageAndRate != null)
            {
                long total = responseCompressed = compressedResponseMeter.getTotal();
                table.addBodyRow(new Row().add(
                        "Response content"
                        ,DOUBLE_FORMAT.format(averageAndRate.getAverage())
                        ,DOUBLE_FORMAT.format(averageAndRate.getStandardDeviation())
                        ,total
                        ,total / 1024
                        ,total / 1024 / 1024
                        ,total / 1024 / 1024 / 1024
                        ,total / 1024 / 1024 / 1024 / 1024));
            }
            else
            {
                long total = responseCompressed = compressedResponseMeter.getTotal();
                table.addBodyRow(new Row().add(
                        "Response content"
                        ,""
                        ,""
                        ,total
                        ,total / 1024
                        ,total / 1024 / 1024
                        ,total / 1024 / 1024 / 1024
                        ,total / 1024 / 1024 / 1024 / 1024));
            }

            page.content().addInner(new p());
            Panel ratioPanel=page.content().returnAddInner(new Level1Panel(page.head(), "Compression Ratios"));
            NameValueList list=ratioPanel.content().returnAddInner(new NameValueList());
            list.add("Request", requestUncompressed != 0?DOUBLE_FORMAT.format((double) requestCompressed / requestUncompressed):"");
            list.add("Response",responseUncompressed != 0?DOUBLE_FORMAT.format((double) responseCompressed / responseUncompressed):"");
        }
        return page;
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
    public Element classDefinitions(Context context,@PathParam("server") String server) throws Throwable
    {
        OperatorPage page=buildServerOperatorPage("Interoperability", server);
        
        form_get form=page.content().returnAddInner(new form_get().action("/operator/httpServer/classDefinitions/download/"+server));
        fieldset fieldset=form.returnAddInner(new fieldset());
        fieldset.addInner(new legend().addInner("C# Target"));
        NameInputValueList list=fieldset.returnAddInner(new NameInputValueList());
        SelectOptions options=new SelectOptions();
        options.name("target").id("target");
        options.style("width:400px;");
        for (InteropTarget value:InteropTarget.values())
        {
            options.addOption(value);
        }
        list.add("Code",options);
        list.add("Columns", new input_number().name("columns").id("columns").min(40).style("width:396px;").value(80));
        list.add("Namespace", new input_text().name("namespace").id("namespace").style("width:100%"));
        list.add("",new input_submit().value("Download").style("width:400px;"));
        AjaxButton button = new AjaxButton("button", "Preview", "/operator/httpServer/classDefinitions/preview/"+server);
        list.add("",button);
        button.type("post");
        button.val("namespace", "namespace");
        button.val("target", "target");
        button.val("columns", "columns");
        button.async(true);
        page.content().addInner(new div().id("result"));
        return page;
    }
    
    @POST
    @Path("/operator/httpServer/classDefinitions/preview/{server}")
    @ContentWriters(AjaxQueryResultWriter.class)
    public AjaxQueryResult previewClassDefinitions(Trace parent, Context context, @PathParam("server") String server,@QueryParam("namespace") String namespace,@QueryParam ("columns") int columns,@QueryParam("target") InteropTarget target) throws Throwable
    {
        AjaxQueryResult result = new AjaxQueryResult();
        String text=generateClassDefinitions(server, namespace, columns, target);
        Panel panel=new Panel(null,null,"C# classes");
        panel.content().addInner(new textarea().readonly().style("width:998%;").rows(Utils.occurs(text, "\r")+1).addInner(text));
        result.put("result", panel.toString());
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
    public Element method(Context context,@PathParam("server") String server,@QueryParam("key") String key) throws Throwable
    {
        OperatorPage page=buildServerOperatorPage("Method: "+key,server);
        HttpServer httpServer=getHttpServer(server);
        RequestHandler requestHandler = httpServer.getRequestHandler(key);
        Method method = requestHandler.getMethod();
        String text = getDescription(method);
        if (text != null)
        {
            Panel panel=page.content().returnAddInner(new Level1Panel(page.head(),"Description"));
            panel.addInner(text);
        }
        
        Level1Panel requestPanel=page.content().returnAddInner(new Level1Panel(page.head(),"Request"));

        writeParameterInfos(page.head(),requestPanel, "Query Parameters", requestHandler, ParameterSource.QUERY);
        writeParameterInfos(page.head(),requestPanel, "Path Parameters", requestHandler, ParameterSource.PATH);
        writeParameterInfos(page.head(),requestPanel, "Header Parameters", requestHandler, ParameterSource.HEADER);
        writeParameterInfos(page.head(),requestPanel, "Cookie Parameters", requestHandler, ParameterSource.COOKIE);

        ParameterInfo contentParameterInfo = findContentParameter(requestHandler);
        if (contentParameterInfo != null)
        {
            Level2Panel contentParameterPanel=requestPanel.content().returnAddInner(new Level2Panel(page.head(),"Content Parameter"));

            Parameter contentParameter = method.getParameters()[contentParameterInfo.getIndex()];
            ParameterWriter parameterWriter = new ParameterWriter(page.head(),contentParameterPanel);
            parameterWriter.write(contentParameter.getType());

            if (requestHandler.getContentReaders().size() > 0)
            {
                requestPanel.content().addInner(new p());
                Level2Panel contentReaderPanel=requestPanel.content().returnAddInner(new Level2Panel(page.head(),"Content Readers"));
                for (ContentReader<?> contentReader : requestHandler.getContentReaders().values())
                {
                    WideTable table=contentReaderPanel.content().returnAddInner(new WideTable(page.head()));
                    table.setHeadRow(new Row().add("Class","Media Type"));
                    table.addBodyRow(new Row().add(contentReader.getClass().getName(),contentReader.getMediaType())); 
                    contentReaderPanel.content().addInner(new p());
                    
                    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                    contentReader.writeSchema(byteOutputStream, contentParameter.getType());
                    String schema = new String(byteOutputStream.toByteArray());

                    byteOutputStream.reset();
                    contentReader.writeExample(byteOutputStream, contentParameter.getType());
                    String example = new String(byteOutputStream.toByteArray());
                    if (schema.length() > 0)
                    {
                        Accordion accordion=contentReaderPanel.content().returnAddInner(new Accordion(page.head(), null, false, "Schema"));
                        textarea area=accordion.content().returnAddInner(new textarea().readonly().style("width:100%;").rows(Utils.occurs(schema, "\r")+1));
                        area.addInner(schema);
                    }
                    if (example.length() > 0)
                    {
                        Accordion accordion=contentReaderPanel.content().returnAddInner(new Accordion(page.head(), null, false, "Example"));
                        textarea area=accordion.content().returnAddInner(new textarea().readonly().style("width:100%;").rows(Utils.occurs(example,"\r")+1));
                        area.addInner(example);
                    }
                    contentReaderPanel.content().addInner(new p());
                }
            }
            
        }

        Class<?> returnType = method.getReturnType();
        Type innerReturnType = null;
        if (returnType != void.class)
        {
            Level1Panel responsePanel=page.content().returnAddInner(new Level1Panel(page.head(), "Response"));
            
            Level2Panel returnParameterPanel=responsePanel.content().returnAddInner(new Level2Panel(page.head(),"Return Type"));
            
            ParameterWriter parameterWriter = new ParameterWriter(page.head(),returnParameterPanel);
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
                    ContentWriterList types = lists.get(entry.getValue().getMediaType());
                    if (types == null)
                    {
                        types = new ContentWriterList(entry.getValue());
                        lists.put(entry.getValue().getMediaType(), types);
                    }
                    types.types.add(entry.getKey());
                }

                responsePanel.content().addInner(new p());
                Level2Panel contentWriterPanel=responsePanel.content().returnAddInner(new Level2Panel(page.head(),"Content Writers"));
                for (ContentWriterList list : lists.values())
                {
                    WideTable table=contentWriterPanel.content().returnAddInner(new WideTable(page.head()));
                    table.setHeadRow(new Row().add("Class","Media Type","Accept Types"));
                    table.addBodyRow(new Row().add(list.contentWriter.getClass().getName(),list.contentWriter.getMediaType(),Utils.combine(list.types, ", ")));

                    if (innerReturnType==null)
                    {
                        contentWriterPanel.content().addInner(new p());
                        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                        list.contentWriter.writeSchema(byteOutputStream, returnType);
                        String schema = new String(byteOutputStream.toByteArray());
    
                        byteOutputStream.reset();
                        list.contentWriter.writeExample(byteOutputStream, returnType);
                        String example = new String(byteOutputStream.toByteArray());
                        if (schema.length() > 0)
                        {
                            Accordion accordion=contentWriterPanel.content().returnAddInner(new Accordion(page.head(), null, false, "Schema"));
                            textarea area=accordion.content().returnAddInner(new textarea().readonly().style("width:100%;").rows(Utils.occurs(schema, "\r")+1));
                            area.addInner(schema);
                        }
                        if (example.length() > 0)
                        {
                            Accordion accordion=contentWriterPanel.content().returnAddInner(new Accordion(page.head(), null, false, "Example"));
                            textarea area=accordion.content().returnAddInner(new textarea().readonly().style("width:100%;").rows(Utils.occurs(example,"\r")+1));
                            area.addInner(example);
                        }
                    }
                    contentWriterPanel.content().addInner(new p());
                }
            }
        }

        if (requestHandler.getContentDecoders().size() > 0)
        {
            Level1Panel panel=page.content().returnAddInner(new Level1Panel(page.head(),"Content Decoders"));
            WideTable table=panel.content().returnAddInner(new WideTable(page.head()));
            table.setHeadRow(new Row().add("Class","Content-Encoding","Encoder"));
            for (Entry<String, ContentDecoder> entry : requestHandler.getContentDecoders().entrySet())
            {
                table.addBodyRow(new Row().add(entry.getValue().getClass().getName(),entry.getValue().getCoding(),entry.getKey()));
            }
        }
        if (requestHandler.getContentEncoders().size() > 0)
        {
            Level1Panel panel=page.content().returnAddInner(new Level1Panel(page.head(),"Content Encoders"));
            WideTable table=panel.content().returnAddInner(new WideTable(page.head()));
            table.setHeadRow(new Row().add("Class","Content-Encoding","Encoder"));
            for (Entry<String, ContentEncoder> entry : requestHandler.getContentEncoders().entrySet())
            {
                table.addBodyRow(new Row().add(entry.getValue().getClass().getName(),entry.getValue().getCoding(),entry.getKey()));
            }
        }
        if (requestHandler.getFilters().length > 0)
        {
            Level1Panel panel=page.content().returnAddInner(new Level1Panel(page.head(),"Filters"));
            WideTable table=panel.content().returnAddInner(new WideTable(page.head()));
            Row row=new Row();
            for (Filter filter : requestHandler.getFilters())
            {
                row.add(filter.getClass().getName());
            }
            table.addBodyRow(row);
        }
        Level1Panel methodPanel=page.content().returnAddInner(new Level1Panel(page.head(),"Class Method"));
        methodPanel.content().addInner(Utils.escapeHtml(method.toGenericString()+";"));
        
        Level1Panel executePanel=page.content().returnAddInner(new Level1Panel(page.head(),"Execute: "+key));
        String httpMethod = requestHandler.getHttpMethod();
        {
            form_get form=executePanel.content().returnAddInner(new form_get());
            AjaxButton button = new AjaxButton("button", "Execute call", "/operator/httpServer/method/execute/"+server);
            button.parameter("key", key);
            button.style("height:3em;width:10em;margin:10px;");
            writeInputParameterInfos(page.head(),form, button, "Query Parameters", requestHandler, ParameterSource.QUERY);
            writeInputParameterInfos(page.head(),form, button, "Path Parameters", requestHandler, ParameterSource.PATH);
            writeInputParameterInfos(page.head(),form, button, "Header Parameters", requestHandler, ParameterSource.HEADER);
            writeInputParameterInfos(page.head(),form, button, "Cookie Parameters", requestHandler, ParameterSource.COOKIE);
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
                            fieldset field=form.returnAddInner(new fieldset());
                            field.addInner(new legend().addInner("Request Content"));
                            textarea input=new textarea().id("contentText").style("width:99%;height:10em;");
                            field.addInner(input);
                            button.val("contentText", "contentText");
                        }
                    }
                }
            }
            fieldset field=form.returnAddInner(new fieldset());
            field.addInner(new legend().addInner("Additional Headers"));
            field.addInner(new input_text().name("headers").id("headers").style("width:100%;"));
            
            button.val("headers", "headers");

            NameInputValueList list=new NameInputValueList();
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
                    SelectOptions options=new SelectOptions();
                    options.id("accept");
                    for (String type:set)
                    {
                        options.add(type);
                    }
                    list.add("Accept", options);
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
                SelectOptions options=new SelectOptions();
                options.id("contentType");
                for (String type:set)
                {
                    options.add(type);
                }
                list.add("ContentType", options);
                button.val("contentType", "contentType");
            }
            if (list.size()>0)
            {
                form.addInner(new fieldset()
                        .addInner(new legend().addInner("Media Types"))
                        .addInner(list));
            }
            div buttons=form.returnAddInner(new div()).style("display:inline;");
            button.style("height:3em;width:10em;margin:10px;");
            buttons.addInner(button);
            if ("GET".equals(httpMethod))
            {
                form.action("/operator/httpServer/method/run/"+server);
                form.addInner(new input_hidden().name("key").value(key));
                form.addInner(new input_hidden().name("server").value(server));
              buttons.addInner(new input_submit().value("Run in browser").style("height:3em;width:10em;margin:10px;"));
            }
            executePanel.content().addInner(new p());
            executePanel.content().addInner(new div().id("result"));

        }
        return page;
    }

    static class HttpClientEndPoint
    {
        final HttpClient httpClient;
        final String endPoint;
        HttpClientEndPoint(HttpClient httpClient,String endPoint)
        {
            this.httpClient=httpClient;
            this.endPoint=endPoint;
        }
    }
    
    private HttpClientEndPoint getExecuteClient(HttpServer httpServer,Context context) throws Throwable
    {
        String endPoint = context.getHttpServletRequest().getHeader("Referer");
        int index=endPoint.lastIndexOf(':');
        if (index>0)
        {
            endPoint=endPoint.substring(0,index);
        }
        int[] ports=httpServer.getPorts();
        endPoint=endPoint+":"+ports[0];
        Configuration configuration=this.serverApplication.getConfiguration();
        boolean https=configuration.getBooleanValue("HttpServer.public.https",false);
        if (https)
        {
            if (this.serverApplication.getPublicServer().getPorts()[0]==ports[0])
            {
                Vault vault=this.serverApplication.getVault();
                String serverCertificatePassword=vault.get("KeyStore.serverCertificate.password");
                String clientCertificatePassword=vault.get("KeyStore.clientCertificate.password");
                String serverCertificateKeyStorePath=configuration.getValue("HttpServer.serverCertificate.keyStorePath",null);
                String clientCertificateKeyStorePath=configuration.getValue("HttpServer.clientCertificate.keyStorePath",null);
                String clusterName=configuration.getValue("httpServer.public.clusterName","server");
                endPoint=endPoint.replace("http", "https");
                return new HttpClientEndPoint(HttpClientFactory.createSSLClient(new HttpClientConfiguration(), clientCertificateKeyStorePath, clientCertificatePassword, serverCertificateKeyStorePath, serverCertificatePassword, clusterName),endPoint);
            }
        }
        return new HttpClientEndPoint(HttpClientFactory.createDefaultClient(),endPoint);
    }
    
    //TODO: Use PathAndQueryBuilder to ensure strings are escaped properly.
    @GET
    @Path("/operator/httpServer/method/execute/{server}")
    @ContentWriters(AjaxQueryResultWriter.class)
    public AjaxQueryResult execute(Trace parent, Context context, @PathParam("server") String server,@QueryParam("key") String key) throws Throwable
    {
        try
        {
            HttpServer httpServer=getHttpServer(server);
            HttpServletRequest request = context.getHttpServletRequest();
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

            HttpClientEndPoint httpClientEndPoint=getExecuteClient(httpServer,context);
            TextClient textClient = new TextClient(this.serverApplication.getTraceManager(), this.serverApplication.getLogger(), httpClientEndPoint.endPoint,httpClientEndPoint.httpClient);
            int statusCode;
            TextResponse response=null;
            double duration = 0;
            String accept = request.getParameter("accept");
            if (Strings.isNullOrEmpty(accept)==false)
            {
                headers.add(new Header("Accept", accept));
            }

            if (method.equals("POST"))
            {
                String contentType = request.getParameter("contentType");
                String contentText = request.getParameter("contentText");
                if (Strings.isNullOrEmpty(contentType)==false)
                {
                    headers.add(new Header("Content-Type", contentType));
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
                String browser=request.getParameter("browser");
                try (Trace trace = new Trace(this.serverApplication.getTraceManager(), parent, "Execute"))
                {
                    response = textClient.get(trace, null, pathAndQuery.toString(), headers.toArray(new Header[headers.size()]));
                    statusCode=response.getStatusCode();
                    duration = trace.getDuration();
                }
            }
            else if (method.equals("PUT"))
            {
                String contentType = request.getParameter("contentType");
                String contentText = request.getParameter("contentText");
                if (Strings.isNullOrEmpty(contentType)==false)
                {
                    headers.add(new Header("Content-Type", contentType));
                }
                try (Trace trace = new Trace(this.serverApplication.getTraceManager(), parent, "Execute"))
                {
                    statusCode=textClient.put(trace, null, pathAndQuery.toString(), contentText, headers.toArray(new Header[headers.size()]));
                    duration = trace.getDuration();
                }
            }
            else if (method.equals("PATCH"))
            {
                String contentType = request.getParameter("contentType");
                String contentText = request.getParameter("contentText");
                if (Strings.isNullOrEmpty(contentType)==false)
                {
                    headers.add(new Header("Content-Type", contentType));
                }
                try (Trace trace = new Trace(this.serverApplication.getTraceManager(), parent, "Execute"))
                {
                    statusCode=textClient.patch(trace, null, pathAndQuery.toString(), contentText, headers.toArray(new Header[headers.size()]));
                    duration = trace.getDuration();
                }
            }
            else if (method.equals("DELETE"))
            {
                try (Trace trace = new Trace(this.serverApplication.getTraceManager(), parent, "Execute"))
                {
                    statusCode=textClient.delete(trace, null, pathAndQuery.toString(), headers.toArray(new Header[headers.size()]));
                    duration = trace.getDuration();
                }
            }
            else
            {
                AjaxQueryResult result = new AjaxQueryResult();
                Level2Panel panel=new Level2Panel(null, "Not implemented");
                panel.content().addInner("Method=" + method);
                result.put("result",panel.toString());
                return result;
            }
            AjaxQueryResult result = new AjaxQueryResult();
            Level2Panel resultPanel=new Level2Panel(null, "Result");
            Level3Panel statusPanel=resultPanel.content().returnAddInner(new Level3Panel(null, "Performance and Status"));
            NameValueList list=statusPanel.content().returnAddInner(new NameValueList());
            list.add("Time",Utils.millisToLocalDateTimeString(System.currentTimeMillis()));
            list.add("Duration", duration * 1000 + " ms");
            list.add("Status Code",statusCode);
            if (response!=null)
            {
                if (response.getHeaders().length > 0)
                {
                    resultPanel.content().addInner(new p());
                    Level3Panel panel=resultPanel.content().returnAddInner(new Level3Panel(null, "Response Headers"));
                    NameValueList headerList=panel.content().returnAddInner(new NameValueList());
                    for (Header header : response.getHeaders())
                    {
                        headerList.add(header.getName(),header.getValue());
                    }
                }
                if (response.getText().length() > 0)
                {
                    resultPanel.content().addInner(new p());
                    Level3Panel contentPanel=resultPanel.content().returnAddInner(new Level3Panel(null,"Content"));
                    textarea area=contentPanel.content().returnAddInner(new textarea());
                    String text=response.getText();
                    area.readonly().style("width:100%;").rows(Utils.occurs(text,"\r")+1);
                    area.addInner(text);
                }
            }
            result.put("result", resultPanel.toString());
            return result;
        }
        catch (Throwable t)
        {
            AjaxQueryResult result = new AjaxQueryResult();
            Level2Panel panel=new Level2Panel(null, "Internal Execution Exception");
            String text=Utils.getStrackTraceAsString(t);
            textarea area=panel.content().returnAddInner(new textarea());
            area.readonly().style("width:100%;").rows(Utils.occurs(text, "\r")+1);
            area.addInner(text);
            result.put("result", panel.toString());
            return result;
            
        }
    }
    @GET
    @Path("/operator/httpServer/method/run/{server}")
    @ContentWriters(HtmlElementWriter.class)
    public Element run(Trace parent, Context context, @PathParam("server") String server,@QueryParam("key") String key) throws Throwable
    {
        HttpServer httpServer=getHttpServer(server);
        HttpServletRequest request = context.getHttpServletRequest();
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
        }
        
        HttpClientEndPoint httpClientEndPoint=getExecuteClient(httpServer,context);
        String content=httpClientEndPoint.endPoint+pathAndQuery.toString();
        BasicPage page=new BasicPage();
        page.head().addInner(new meta().http_equiv(http_equiv.refresh).content("0;URL='"+content+"'"));
        return page;
    }

    @GET
    @Path("/")
    public Element main() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Main");
        long now = System.currentTimeMillis();
        page.content().returnAddInner(new NameValueList())
        .add("Started",Utils.millisToLocalDateTimeString(this.serverApplication.getStartTime()))
        .add("Current",Utils.millisToLocalDateTimeString(now))
        .add("Uptime",Utils.millisToDurationString(now - this.serverApplication.getStartTime()));
        return page;
    }

    private static DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#.###");


    private void writeSize(Table table, String label, double value)
    {
        table.addBodyRowItems(label,DOUBLE_FORMAT.format(value),DOUBLE_FORMAT.format(value / 1024),DOUBLE_FORMAT.format(value / 1024 / 1024)
                ,DOUBLE_FORMAT.format(value / 1024 / 1024 / 1024));
    }

    private void write(Table table, String label, LevelMeter meter)
    {
        table.addBodyRowItems(label,meter.getLevel(),meter.getMaximumLevel(),Utils.millisToLocalDateTime(meter.getHighestLevelTimeStamp()));
    }

    private void write(Table table, String label, CountMeter meter)
    {
        table.addBodyRowItems(label,meter.getCount(),"","");
    }

    @GET
    @Path("/operator/logging/status")
    public Element loggingStatus() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Logging Status");
        LogDirectoryManager manager = this.serverApplication.getLogDirectoryManager();
        if (manager != null)
        {
            if (this.serverApplication.getLogQueue() instanceof JSONBufferedLZ4Queue)
            {
                JSONBufferedLZ4Queue sink = (JSONBufferedLZ4Queue) this.serverApplication.getLogQueue();
                {
                    Panel panel=page.content().returnAddInner(new Level1Panel(page.head(),"Logger"));
                    {
                        Panel statsPanel=panel.content().returnAddInner(new Level2Panel(page.head(), "Worker Stats"));
                        Table table=statsPanel.content().returnAddInner(new WideTable(page.head()));
                        table.setHeadRowItems("Name","Value","Max","Max Instant");
                        write(table,"Thread Workers Used",sink.getThreadWorkerQueueInUseMeter());
                        write(table,"Waiting in Thread Workers",sink.getThreadWorkerQueueWaitingMeter());
                        write(table,"Stalled in Thread Workers",sink.getThreadWorkerQueueStalledMeter());
                        write(table,"Dropped in Thread Workers",sink.getThreadWorkerQueueDroppedMeter());
                        write(table,"Waiting in Source",sink.getWaitingMeter());
                        write(table,"Stalled in Source ",sink.getStalledMeter());
                        write(table,"Dropped in Source ",sink.getDroppedMeter());
                    }
                    {
                        panel.content().addInner(new p());
                        Panel performancePanel=panel.content().returnAddInner(new Level2Panel(page.head(),"Performance"));
                        Table table=performancePanel.content().returnAddInner(new WideTable(page.head()));
                        table.setHeadRowItems("","Bytes","KB","MB","GB");
                    
                        writeSize(table, "Write Rate (per second)", sink.getWriteRateMeter().sampleRate(this.rateSamplingDuration));
                        writeSize(table, "Written", sink.getWriteRateMeter().getCount());
                    }
                }
            }
            
            {
                Panel panel=page.content().returnAddInner(new Level1Panel(page.head(),"Volume"));

                Panel infoPanel=panel.content().returnAddInner(new Level2Panel(page.head(), "Info"));
                LogDirectoryInfo info = manager.getLogDirectoryInfo();
                NameValueList list=infoPanel.content().returnAddInner(new NameValueList());
                list.addInner(new style().addInner("thead {background-color:#eee;} td {border:1px solid #888;padding:4px;} table{border-collapse:collapse;} "));
                list.add("Path",manager.getFullDirectoryPath());
                list.add("Deletes due to directory size exceeded",manager.getDirectorySizeDeleteMeter().getCount());
                list.add("Deletes due to maximum files exceeded",manager.getMaximumFilesDeleteMeter().getCount());
                list.add("Deletes due to reserve space exceeded",manager.getReserveSpaceDeleteMeter().getCount());
                list.add("File delete failures",manager.getFileDeleteFailedMeter().getCount());
                list.add("Number of files",info.getFileCount());
                list.add("Oldest file name",info.getOldestFileName());
                list.add("Oldest file date",Utils.millisToLocalDateTimeString(info.getOldestFileDate()));
                list.add("Newest file name",info.getNewestFileName());
                list.add("Newest file date",Utils.millisToLocalDateTimeString(info.getNewestFileDate()));

                panel.content().addInner(new p());
                Panel usagePanel=panel.content().returnAddInner(new Level2Panel(page.head(), "Usage"));
                Table table=usagePanel.content().returnAddInner(new WideTable(page.head()));
                table.setHeadRowItems("","Bytes","KB","MB","GB");
                writeSize(table, "Directory size", info.getDirectorySize());
                writeSize(table, "Volume free space", info.getFreeSpace());
                writeSize(table, "Volume free space", info.getFreeSpace());
                writeSize(table, "Volume usable space", info.getUsableSpace());
                writeSize(table, "Volume total space", info.getTotalSpace());
                if (info.getFileCount() > 0)
                {
                    writeSize(table, "Oldest file size", info.getOldestFileSize());
                    writeSize(table, "Newest file size", info.getNewestFileSize());
                }
                if (info.getThrowable() != null)
                {
                    panel.content().addInner(new p());
                    Panel exceptionPanel=page.content().returnAddInner(new Level2Panel(page.head(),"Exception"));
                    exceptionPanel.content().addInner(Utils.toString(info.getThrowable()));
                }
            }
        }
        return page;
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
    @Path("/resources/{+}")
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
