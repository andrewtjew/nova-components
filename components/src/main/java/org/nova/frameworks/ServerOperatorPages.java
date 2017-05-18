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
import org.nova.html.Attribute;
import org.nova.html.HtmlWriter;
import org.nova.html.Selection;
import org.nova.html.TableList;
import org.nova.html.operator.Menu;
import org.nova.html.operator.OperatorResult;
import org.nova.html.operator.OperatorResultWriter;
import org.nova.html.tags.a;
import org.nova.html.tags.area;
import org.nova.html.tags.div;
import org.nova.html.tags.fieldset;
import org.nova.html.tags.form_post;
import org.nova.html.tags.h3;
import org.nova.html.tags.hr;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_submit;
import org.nova.html.tags.input_text;
import org.nova.html.tags.legend;
import org.nova.html.tags.p;
import org.nova.html.tags.span;
import org.nova.html.tags.style;
import org.nova.html.tags.td;
import org.nova.html.tags.textarea;
import org.nova.html.tags.tr;
import org.nova.html.widgets.AjaxButton;
import org.nova.html.widgets.AjaxQueryResult;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.html.widgets.DataTable;
import org.nova.html.widgets.Head;
import org.nova.html.widgets.MenuBar;
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
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.elements.InnerElement;
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
import com.mysql.fabric.xmlrpc.base.Data;

@Description("Handlers for server operator pages")
@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({ JSONContentReader.class, JSONPatchContentReader.class })
@ContentWriters({OperatorResultWriter.class, HtmlContentWriter.class, HtmlElementWriter.class})
public class ServerOperatorPages
{
    static class WideTable extends Table
    {
        public WideTable()
        {
            super();
            addInner(new style().addInner("thead {background-color:#eee;} td {border:1px solid #888;padding:4px;} table{border-collapse:collapse;width:100%;} "));
            thead().style("background-color:#eee");
        }
    }
    static class Panel1 extends Panel
    {
        public Panel1(Head head,String title)
        {
            super(head,null,title);
        }
    }
    static class Panel2 extends Panel1
    {
        public Panel2(Head head,String title)
        {
            super(head,title);
            style("padding:4px;");
            this.heading().style("background-color:#ddd;color:#000;text-align:left;padding:2px 2px 2px 8px;");
            this.content().style("padding:0px;");
        }
    }
    static class Panel3 extends Panel1
    {
        public Panel3(Head head,String title)
        {
            super(head,title);
            style("padding:4px;");
            this.heading().style("background-color:#fff;color:#000;text-align:left;font-weight:normal;padding:2px 2px 2px 8px;");
            this.content().style("padding:4px;");
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

        Menu menu = serverApplication.getOperatorResultWriter().getMenu();

        menu.add("Process|Configuration", "/operator/application/configuration");
        menu.add("Process|Futures", "/operator/application/futures");
        menu.add("Process|Timer Tasks", "/operator/application/timers");
        menu.add("Process|Meters|Categories", "/operator/meters/categories");
        menu.add("Process|Meters|Level Meters", "/operator/meters/levelMeters");
        menu.add("Process|Meters|Rate Meters", "/operator/meters/rateMeters");
        menu.add("Process|Meters|Count Meters", "/operator/meters/countMeters");
        menu.add("Process|Meters|CountAverageRate Meters", "/operator/meters/countAverageRateMeters");

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

    private DataTable createStandardTable(Head head,String id)
    {
        DataTable table=new DataTable(head,id);
        table.lengthMenu(-1,16,32,64);
        return table;
    }
    
    @GET
    @Path("/operator/application/configuration")
    public Element configuration() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Configuration"); 
        DataTable table=page.content().returnAddInner(createStandardTable(page.head(),null));
        
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
        DataTable table=page.content().returnAddInner(createStandardTable(page.head(),null));

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
        DataTable table=page.content().returnAddInner(createStandardTable(page.head(),null));
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
        boolean open=true;
        LevelMeterBox[] levelBoxes = snapshot.getLevelMeterBoxes();
        if (levelBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, open,"Level Meters"));
            DataTable table=accordion.panel().returnAddInner(createStandardTable(page.head(),null));
            table.setHeadRow(new Row().add("Category","Name","Level", "Highest","Highest TimeStamp","Description"));
        
            for (LevelMeterBox box : levelBoxes)
            {
                LevelMeter meter = box.getMeter();
                table.addBodyRowItems(box.getCategory(),box.getName(),meter.getLevel(),meter.getMaximumLevel()
                        ,Utils.millisToLocalDateTimeString(meter.getHighestLevelTimeStamp()),box.getDescription());
            }
            open=false;
        } 
        RateMeterBox[] rateBoxes = snapshot.getRateMeterBoxes();
        if (rateBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, open,"Rate Meters"));
            DataTable table=accordion.panel().returnAddInner(createStandardTable(page.head(),null));
            table.setHeadRow(new Row().add("Name","Rate","Count","Average","Description"));
            for (RateMeterBox box : rateBoxes)
            {
                RateMeter meter = box.getMeter();
                table.addBodyRowItems(box.getName(),String.format("%.4f", meter.sampleRate(interval))
                ,meter.getCount(),String.format("%.4f", meter.sampleRate(interval)),(box.getDescription()));
            }
            open=false;
        }
        CountMeterBox[] countBoxes = snapshot.getCountMeterBoxes();
        if (countBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, open,"Count Meters"));
            DataTable table=accordion.panel().returnAddInner(createStandardTable(page.head(),null));
            table.setHeadRow(new Row().add("Name","Count","Description"));
            for (CountMeterBox box : countBoxes)
            {
                CountMeter meter = box.getMeter();
                table.addBodyRowItems(box.getName(),meter.getCount(),box.getDescription());
            }
            open=false;
        }
        CountAverageRateMeterBox[] countAverageRateBoxes = snapshot.getCountAverageRateMeterBoxes();
        if (countAverageRateBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, open,"CountAverageRate Meters"));
            DataTable table=accordion.panel().returnAddInner(createStandardTable(page.head(),null));
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
            open=false;
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
        boolean open=true;
        LevelMeterBox[] levelBoxes = categories.getLevelMeterBoxes();
        if (levelBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, open,"Level Meters"));
            DataTable table=accordion.panel().returnAddInner(createStandardTable(page.head(),null));
            table.setHeadRow(new Row().add("Category","Name","Level", "Highest","Highest TimeStamp","Description"));
        
            for (LevelMeterBox box : levelBoxes)
            {
                LevelMeter meter = box.getMeter();
                table.addBodyRowItems(box.getCategory(),box.getName(),meter.getLevel(),meter.getMaximumLevel()
                        ,Utils.millisToLocalDateTimeString(meter.getHighestLevelTimeStamp()),box.getDescription());
            }
            open=false;
        }        
        RateMeterBox[] rateBoxes = categories.getRateMeterBoxes();
        if (rateBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, open,"Rate Meters"));
            accordion.button().addInner("Rate Meters");
            DataTable table=accordion.panel().returnAddInner(createStandardTable(page.head(),null));
            table.setHeadRow(new Row().add("Name","Rate","Count","Average","Description"));
            for (RateMeterBox box : rateBoxes)
            {
                RateMeter meter = box.getMeter();
                table.addBodyRowItems(box.getName(),String.format("%.4f", meter.sampleRate(interval))
                ,meter.getCount(),String.format("%.4f", meter.sampleRate(interval)),(box.getDescription()));
            }
            open=false;
        }

        CountMeterBox[] countBoxes = categories.getCountMeterBoxes();
        if (countBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null,open,"Count Meters"));
            DataTable table=accordion.panel().returnAddInner(createStandardTable(page.head(),null));
            table.setHeadRow(new Row().add("Name","Count","Description"));
            for (CountMeterBox box : countBoxes)
            {
                CountMeter meter = box.getMeter();
                table.addBodyRowItems(box.getName(),meter.getCount(),box.getDescription());
            }
            open=false;
        }
        CountAverageRateMeterBox[] countAverageRateBoxes = categories.getCountAverageMeterBoxes();
        if (countAverageRateBoxes.length>0)
        {
            Accordion accordion=page.content().returnAddInner(new Accordion(page.head(), null, open,"CountAverageRate Meters"));
            DataTable table=accordion.panel().returnAddInner(createStandardTable(page.head(),null));
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
            open=false;
        }
        return page;
    }

    @GET
    @Path("/operator/application/meters/categories")
    public Element meterCategories() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Meter Categories");
        Panel panel=page.content().returnAddInner(new Panel(page.head(),"panel","Categories"));
        String[] categories = this.serverApplication.getMeterManager().getSnapshot().getCategories();
        VerticalMenu menu=panel.content().returnAddInner(new VerticalMenu(page.head(), "menu"));
        for (String category:categories)
        {
            menu.addMenuItem(category, new PathAndQueryBuilder("/operator/application/meters/category").addQuery("category", category).toString());
        }
        return page;
    }
    
    //--- old
    void write(HtmlWriter writer, Trace trace, Object family)
    {
        writer.tr(writer.inner().td(family).td(writer.inner().a(trace.getCategory(), "./activeTrace/" + trace.getNumber())).td(trace.getNumber())
                .td(Utils.millisToLocalDateTime(trace.getCreated())).td(trace.getActiveNs() / 1000000).td(trace.getWaitNs() / 1000000).td(trace.getDurationNs() / 1000000)
                .td(trace.isWaiting()).td(trace.getDetails()).td(trace.getThread().getName()));

    }

    @GET
    @Path("/operator/tracing/activeTrace")
    public Response<OperatorResult> activeTrace(@QueryParam("number") long number)
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
        return OperatorResult.respond(writer, "Active Trace");
    }

    @GET
    @Path("/operator/tracing/activeStats")
    public Element activeTraceStats() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Active Trace Stats");
        Trace[] traces = this.serverApplication.getTraceManager().getActiveSnapshot();
        DataTable table=page.content().returnAddInner(createStandardTable(page.head(),null));
        table.setHeadRow(new Row().add("Category","Number").addWithTitle("Active", "milliseconds").addWithTitle("Wait", "milliseconds").addWithTitle("Duration", "milliseconds").add("Waiting","Details","Created"));
        for (Trace trace : traces)
        {
            Row row=new Row().add(trace.getCategory(), trace.getNumber(),trace.getActiveNs() / 1000000,trace.getWaitNs() / 1000000
                    ,trace.getDurationNs() / 1000000,trace.isWaiting(),trace.getDetails(),Utils.millisToLocalDateTime(trace.getCreated()));
            row.onClickLocation(new PathAndQueryBuilder("./activeTrace").addQuery("number", trace.getNumber()).toString());
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
        DataTable table=page.content().returnAddInner(createStandardTable(page.head(),null));

        table.setHeadRow(new Row().add("Category","Count")
                .addWithTitle("Average", "milliseconds")
                .addWithTitle("Stddev", "Standard deviation in milliseconds")
                .addWithTitle("Total", "Total duration in milliseconds"));
        for (TraceStats item : array)
        {
            CountAverageRateMeter meter = item.getMeter();
            AverageAndRate ar = meter.getMarkCountAverage(this.rateSamplingDuration);
            Row row=new Row().add(item.getCategory()
                    ,meter.getCount()
                    ,nanoToDefaultFormat(ar.getAverage())
                    ,nanoToDefaultFormat(ar.getStandardDeviation())
                    ,nanoToDefaultFormat(meter.getTotal()));

            row.onClickLocation(new PathAndQueryBuilder("./trace").addQuery("category", item.getCategory()).toString());
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
        DataTable table=page.content().returnAddInner(this.createStandardTable(page.head(), null));
        table.setHeadRow(new Row().add("Category","Count")
                .addWithTitle("Average", "Milliseconds")
                .addWithTitle("Duration", "Milliseconds")
                .addWithTitle("Wait", "Milliseconds")
                );

        for (Entry<String, TraceNode> entry : map.entrySet())
        {
            TraceNode node = entry.getValue();
            Row row=new Row().add(entry.getKey(),node.getCount()
                    ,nanoToDefaultFormat(node.getTotalDurationNs() / node.getCount())
                    ,nanoToDefaultFormat(node.getTotalDurationNs())
                    ,nanoToDefaultFormat(node.getTotalWaitNs()));
            row.onclick("window.location='"+new PathAndQueryBuilder("./trace").addQuery("category", entry.getKey()).toString()+"'");
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
          
//          writer.a(entry.getKey(), new PathAndQueryBuilder("./trace").addQuery("category", entry.getKey()).toString());
//          tr.onclick("window.location='"+new PathAndQueryBuilder("./trace").addQuery("category", entry.getKey()).toString()+"'");
          Panel panel=td.returnAddInner(new Panel(head,null,null));
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
          div twoPanel=panel.content().returnAddInner(new div()).style("display:flex;");
          NameValueList left=twoPanel.returnAddInner(new NameValueList(": "));
          left.style("width:200px;");
          left.add("Count", node.getCount());
          left.add("Average", nanoToDefaultFormat(node.getTotalDurationNs() / node.getCount()));  //Milliseconds
          twoPanel.returnAddInner(new div().style("width:10px;"));
          NameValueList right=twoPanel.returnAddInner(new NameValueList(": "));
          right.style("width:200px;");
          right.add("Duration", nanoToDefaultFormat(node.getTotalDurationNs()));
          right.add("Wait", nanoToDefaultFormat(node.getTotalWaitNs()));
      }
      
      private void writeTraceGraphNode(Head head,tr tr, Entry<String, TraceNode> entry, int level) throws Exception
      {
          TraceNode node = entry.getValue();
          td td=tr.returnAddInner(new td());
          td.style(this.getTraceBoxStyle());
          Panel panel=td.returnAddInner(new Panel(head,null,null));
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
        DataTable table=page.content().returnAddInner(createStandardTable(page.head(),null));
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

            Row row=new Row().add(entry.getKey(), count,nanoToDefaultFormat(totalDurationNs),nanoToDefaultFormat(totalWaitNs),list.size(),map.containsKey(entry.getKey()));
            row.onclick("window.location='"+new PathAndQueryBuilder("./trace").addQuery("category", entry.getKey()).toString()+"'");
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
            page.content().addInner(new p());
            Trace trace = traces[i];
            Panel panel=page.content().returnAddInner(new Panel(page.head(), null, trace.getCategory()));
            Table table=panel.content().returnAddInner(new Table());
            int width=100/9;
            table.addInner(new style().addInner("thead {background-color:#afa;} td {border:1px solid #888;padding:4px;width:auto;} table{border-collapse:collapse;width:100%;} "));
            Row headRow=new Row();
//            headRow.addInner(new td().style("width:12em;").addInner("Created"));
            headRow.add("Created");
            headRow.addWithTitle("Number", "Sequence number")
                .addWithTitle("Parent", "Parent sequence number if trace has parent")
                .addWithTitle("Duration", "Milliseconds")
                .addWithTitle("Wait", "Milliseconds")
                .add("Waiting","Closed","Thread","ThreadId");
            table.setHeadRow(headRow);
            panel.content().addInner(new p());
            
            tr tr=new tr();
            Row row=new Row().add(Utils.millisToLocalDateTimeString(trace.getCreated())
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
                    list=panel.content().returnAddInner(new NameValueList(": "));
                }
                list.add("Details",details);
            }

            String fromLink = trace.getFromLink();
            if (fromLink != null)
            {
                if (list==null)
                {
                    list=panel.content().returnAddInner(new NameValueList(": "));
                }
                list.add("FromLink",fromLink);
            }

            String toLink = trace.getToLink();
            if (toLink != null)
            {
                if (list==null)
                {
                    list=panel.content().returnAddInner(new NameValueList(": "));
                }
                list.add("ToLink",toLink);
            }

            StackTraceElement[] currentStackTrace = trace.getThread().getStackTrace();
            if (currentStackTrace != null)
            {
                Accordion accordion=panel.content().returnAddInner(new Accordion(page.head(), null, false, "Current Stack Trace"));
                accordion.panel().addInner(toString(currentStackTrace, 0));
            }

            StackTraceElement[] createStackTrace = trace.getCreateStackTrace();
            if (createStackTrace != null)
            {
                Accordion accordion=panel.content().returnAddInner(new Accordion(page.head(), null, false, "Create Stack Trace"));
                accordion.panel().addInner(toString(createStackTrace, 0));
            }

            StackTraceElement[] closeStackTrace = trace.getCloseStackTrace();
            if (closeStackTrace != null)
            {
                Accordion accordion=panel.content().returnAddInner(new Accordion(page.head(), null, false, "Close Stack Trace"));
                accordion.panel().addInner(toString(closeStackTrace, 0));
            }

            Throwable exception = trace.getThrowable();
            if (exception != null)
            {
                Accordion accordion=panel.content().returnAddInner(new Accordion(page.head(), null, true, "Exception"));
                accordion.panel().addInner(Utils.toString(exception));
            }
        }
    }


    @GET
    @Path("/operator/meters/levelMeters")
    public Response<OperatorResult> levelMeters(@QueryParam("interval") @DefaultValue("10") long interval)
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
        return OperatorResult.respond(writer, "Level Meters");
    }

    @GET
    @Path("/operator/meters/rateMeters")
    public Response<OperatorResult> countMeters(@QueryParam("interval") @DefaultValue("10") long interval)
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
        return OperatorResult.respond(writer, "Rate Meters");
    }

    @GET
    @Path("/operator/meters/countMeters")
    public Response<OperatorResult> countMeters()
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
        return OperatorResult.respond(writer, "Count Meters");
    }

    @GET
    @Path("/operator/meters/countAverageRateMeters")
    public Response<OperatorResult> countAverageRateMeters(@QueryParam("interval") @DefaultValue("10") long interval)
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
        return OperatorResult.respond(writer, "Count Average Rate Meters");
    }

    @GET
    @Path("/operator/httpServer/performance/{server}")
    public Element performance(@PathParam("server")String server) throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Server Performance: "+server);
        
        HttpServer httpServer=getHttpServer(server);
        RateMeter requestRateMeter = httpServer.getRequestRateMeter();
        double requestRate = requestRateMeter.sampleRate(this.rateSamplingDuration);
        Table infoTable=page.content().returnAddInner(new Table());
        infoTable.addInner(new style().addInner("thead {background-color:#eee;} td {border:1px solid #888;padding:4px;width:auto;} table{border-collapse:collapse;width:100%;} "));
        infoTable.setHeadRowItems("Request Rate","Total Requests","Ports");
        infoTable.addBodyRowItems(DOUBLE_FORMAT.format(requestRate),requestRateMeter.getCount(),Utils.combine(Utils.intArrayToList(httpServer.getPorts()),","));
        
        page.content().addInner(new p());
        Panel requestHandlerPanel=page.content().returnAddInner(new Panel(page.head(),null,"RequestHandlers"));
        DataTable table=requestHandlerPanel.returnAddInner(createStandardTable(page.head(), null));
        RequestHandler[] requestHandlers = httpServer.getRequestHandlers();
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
            row.onclick("window.location='"+new PathAndQueryBuilder("/operator/httpServer/info").addQuery("key", requestHandler.getKey()).addQuery("server", server).toString()+"'");
            row.add(requestHandler.getHttpMethod() + " " + requestHandler.getPath())
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

//    @GET
//    @Path("/operator/httpServer/performance/{server}")
//    public Response<OperatorResult> performance(@PathParam("server")String server) throws Exception
//    {
//        HtmlWriter writer = new HtmlWriter();
//        HttpServer httpServer=getHttpServer(server);
//        RateMeter requestRateMeter = httpServer.getRequestRateMeter();
//        double requestRate = requestRateMeter.sampleRate(this.rateSamplingDuration);
//        writer.begin_table(0);
//        writer.tableList("Request Rate", DOUBLE_FORMAT.format(requestRate));
//        writer.tableList("Total Requests", requestRateMeter.getCount());
//        writer.end_table();
//
//        RequestHandler[] requestHandlers = httpServer.getRequestHandlers();
//        writer.h2("Request Handlers");
//        writer.p("Ports: "+Utils.combine(Utils.intArrayToList(httpServer.getPorts()),",")+", Count:" + requestHandlers.length);
//        if (requestHandlers.length > 0)
//        {
//            writer.begin_sortableTable(1);
//            writer.tr(writer.inner().th("Method").th("Count", "Count of total requests").th("% Count", "Count percentage")
//                    .th("Duration", "Total duration in method in days hours:minutes:seconds.milliseconds").th("% Dur", "Percentage Duration")
//                    .th("Ave Dur", "Average duration of request in milliseconds").th("Rate", "Request rate per second").th("ReqSize", "Average uncompressed size of request content in bytes")
//                    .th("RespSize", "Average uncompressed size of response content in bytes"));
//            double totalAll = 0;
//            long totalCount = 0;
//            for (RequestHandler requestHandler : requestHandlers)
//            {
//                Map<Integer, CountAverageRateMeter> meters = requestHandler.getStatusMeters();
//                for (CountAverageRateMeter meter : meters.values())
//                {
//                    totalAll += meter.getTotal();
//                    totalCount += meter.getCount();
//                }
//            }
//            for (RequestHandler requestHandler : requestHandlers)
//            {
//                Map<Integer, CountAverageRateMeter> meters = requestHandler.getStatusMeters();
//                long total = 0;
//                long count = 0;
//                double rate = 0;
//                for (CountAverageRateMeter meter : meters.values())
//                {
//                    total += meter.getTotal();
//                    count += meter.getCount();
//                    AverageAndRate averageAndRate = meter.getCountAverageRate(this.rateSamplingDuration);
//                    if (averageAndRate != null)
//                    {
//                        rate += averageAndRate.getRate();
//                    }
//                }
//                double countPercentage = totalCount > 0 ? (100.0 * count) / totalCount : 0;
//                double totalMilliseconds = total / 1.0e6;
//                double totalPercentage = totalAll > 0 ? 100.0 * total / totalAll : 0;
//                writer.tr(writer
//                        .inner().td(
//                                writer.inner()
//                                        .a(requestHandler.getHttpMethod() + " " + requestHandler.getPath(),
//                                                new PathAndQueryBuilder("/operator/httpServer/info").addQuery("key", requestHandler.getKey()).toString())
//                                        .td(count).td(DOUBLE_FORMAT.format(countPercentage)).td(Utils.millisToDurationString((long) totalMilliseconds)).td(DOUBLE_FORMAT.format(totalPercentage))
//                                        .td(count > 0 ? DOUBLE_FORMAT.format(total / (1.0e6 * count)) : 0).td(DOUBLE_FORMAT.format(rate)))
//                        .td(count > 0 ? requestHandler.getRequestUncompressedContentSizeMeter().getTotal() / count : 0)
//                        .td(count > 0 ? requestHandler.getResponseUncompressedContentSizeMeter().getTotal() / count : 0));
//            }
//            writer.end_table();
//        }
//        return OperatorResult.respond(writer, "Performance: "+server);
//    }
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
    public Response<OperatorResult> lastRequests(@PathParam("server") String server) throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        HttpServer httpServer=this.getHttpServer(server);
        RequestLogEntry[] entries = httpServer.getLastRequestLogEntries();
        writer.p("Ports: "+Utils.combine(Utils.intArrayToList(httpServer.getPorts()),",")+", Count:" + entries.length);
        writeRequestLogEntries(writer, entries);
        return OperatorResult.respond(writer, "Last Requests: "+server);
    }

    @GET
    @Path("/operator/httpServer/lastExceptionRequests/{server}")
    public Response<OperatorResult> lastExceptionRequests(@PathParam("server") String server) throws Exception
    {
        HtmlWriter writer = new HtmlWriter();
        HttpServer httpServer=this.getHttpServer(server);
        RequestLogEntry[] entries = httpServer.getLastExceptionRequestLogEntries();
        writer.p("Ports: "+Utils.combine(Utils.intArrayToList(httpServer.getPorts()),",")+", Count:" + entries.length);
        writeRequestLogEntries(writer, entries);
        return OperatorResult.respond(writer, "Last Exception Requests: "+server);
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
            Panel panel=page.content().returnAddInner(new Panel(page.head(),null,title));
            Table table=panel.content().returnAddInner(new Table());
            table.addInner(new style().addInner("thead {background-color:#eee;} td {border:1px solid #888;padding:4px;width:auto;} table{border-collapse:collapse;width:100%;} "));
            Row headRow=new Row();
            headRow.addWithTitle("Number", "Trace number")
                .add("Created")
                .addWithTitle("Duration", "milliseconds")
                .addWithTitle("Wait", "Amount of time spent waiting in milliseconds")
                .add("FromLink","Remote","QueryString");
            table.setHeadRow(headRow);
            table.addBodyRowItems(trace.getNumber()
                    ,Utils.millisToLocalDateTimeString(trace.getCreated())
                    ,DOUBLE_FORMAT.format(trace.getDuration() * 1000.0)
                    ,DOUBLE_FORMAT.format(trace.getWait() * 1000.0)
                    ,trace.getFromLink()
                    ,entry.getRemoteEndPoint()
                    ,entry.getQueryString());
            String headers=entry.getRequestHeaders();
            panel.content().addInner(new p());
            if ((headers!=null)&&(headers.length()>0))
            {
                Accordion accordion=panel.content().returnAddInner(new Accordion(page.head(), null,false, "Request"));
                NameValueList list=accordion.panel().returnAddInner(new NameValueList(": "));
                //            writer.textarea(value, new Attribute("style", "width:98%"), new Attribute("cols", cols), new Attribute("rows", rows), new Attribute("readonly", true));

                textarea area=new textarea().readonly().style("width:100%");
                area.addInner(headers);
                list.add("Headers", area);
            }
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
        DataTable table=page.content().returnAddInner(createStandardTable(page.head(),null));
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
            row.onclick("window.location='"+new PathAndQueryBuilder("/operator/httpServer/info").addQuery("key", requestHandler.getKey()).addQuery("server", server).toString()+"'");
            row.add(requestHandler.getHttpMethod(),requestHandler.getPath()
                ,statusCodes.get(200)
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
        DataTable table=page.content().returnAddInner(createStandardTable(page.head(), null));
        table.setHeadRow(new Row().add("Method","Path","Description","Filters"));
        for (RequestHandler requestHandler : requestHandlers)
        {
            Row row=new Row().add(requestHandler.getHttpMethod(),requestHandler.getPath());
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
            row.onclick("window.location='"+new PathAndQueryBuilder("/operator/httpServer/method/"+server).addQuery("key", requestHandler.getKey()).toString()+"'");
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
        Panel2 panel2=panel.content().returnAddInner(new Panel2(head, heading));
        
        WideTable table=panel2.content().returnAddInner(new WideTable());
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

    private void writeInputParameterInfos(Head head,fieldset fieldset, AjaxButton button, String heading, RequestHandler handler, ParameterSource filter)
    {
        if (Arrays.stream(handler.getParameterInfos()).filter(info ->
        {
            return info.getSource() == filter;
        }).count() == 0)
        {
            return;
        }

        Method method = handler.getMethod();
        Panel3 panel=fieldset.returnAddInner(new Panel3(head, heading));
        WideTable table=panel.content().returnAddInner(new WideTable());
        table.setHeadRow(new Row().add("Name","Type","Description","Default","Value"));
        for (ParameterInfo info : handler.getParameterInfos())
        {
            if (info.getSource() == filter)
            {
                String name = info.getName();
                Parameter parameter = method.getParameters()[info.getIndex()];
                Row row=new Row().add(info.getName(),parameter.getType().getName(),getDescription(parameter),info.getDefaultValue());
                table.addBodyRow(row);
                String key = filter.toString() + name;
                if (parameter.getType() == boolean.class)
                {
                    button.val(key, key);
                    row.add(new input_checkbox().id(key).checked((boolean)info.getDefaultValue()));
                }
                else
                {
                    button.val(key, key);
                    row.add(new input_text().id(key).style("background-color:#ffa;width:100%;").placeholder(info.getDefaultValue() == null ? "" : info.getDefaultValue().toString()));
                }
            }
        }
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
            Panel3 panel=this.parentPanel.content().returnAddInner(new Panel3(this.head,"Class: "+type.getName()));
            Description description = type.getAnnotation(Description.class);
            if (description != null)
            {
                panel.content().addInner(description.value());
            }
            WideTable table=panel.content().returnAddInner(new WideTable());
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
    public Response<OperatorResult> info(@QueryParam("key") String key,@QueryParam("server") String server) throws Throwable
    {
        HtmlWriter writer = new HtmlWriter();
        HttpServer httpServer=getHttpServer(server);
        RequestHandler requestHandler = httpServer.getRequestHandler(key);

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

        return OperatorResult.respond(writer, "Method: " + key);
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
    public Response<OperatorResult> classDefinitions(Context context,@PathParam("server") String server) throws Throwable
    {
        HtmlWriter writer = new HtmlWriter();
        writer.begin_form("/operator/httpServer/classDefinitions/download/"+server, "get");
        writer.begin_table();
        writer.tr().td("Target").td(":");
        writer.begin_td();
        Selection selection=new Selection("target",new Attribute("style", "width:100%;"));
        selection.options(InteropTarget.values());
        writer.writeObject(selection);
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
        writer.td(new input_submit().value("Download"));

        AjaxButton button = new AjaxButton("button", "Preview", "/operator/httpServer/classDefinitions/preview/"+server);
        button.type("post");
        button.val("namespace", "namespace");
        button.val("target", "target");
        button.val("columns", "columns");
        button.async(true);
        writer.td(writer.inner().writeObject(button));
        writer.end_td();
        writer.end_table();
        writer.end_form();
        writer.hr();
        
        writer.div(null, new Attribute("id", "result"));
        
        return OperatorResult.respond(writer, "Generate Interop Classes: " + server);
    }
    
    @POST
    @Path("/operator/httpServer/classDefinitions/preview/{server}")
    @ContentWriters(AjaxQueryResultWriter.class)
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
    public Element method(Context context,@PathParam("server") String server,@QueryParam("key") String key) throws Throwable
    {
        OperatorPage page=buildServerOperatorPage("Method: "+key,server);
        HttpServer httpServer=getHttpServer(server);
        HttpServletRequest request=context.getHttpServletRequest();
        RequestHandler requestHandler = httpServer.getRequestHandler(key);
        Method method = requestHandler.getMethod();
        String text = getDescription(method);
        if (text != null)
        {
            Panel panel=page.content().returnAddInner(new Panel(page.head(),null,"Description"));
            panel.addInner(text);
        }
        
        Panel1 requestPanel=page.content().returnAddInner(new Panel1(page.head(),"Request"));

        writeParameterInfos(page.head(),requestPanel, "Query Parameters", requestHandler, ParameterSource.QUERY);
        writeParameterInfos(page.head(),requestPanel, "Path Parameters", requestHandler, ParameterSource.PATH);
        writeParameterInfos(page.head(),requestPanel, "Header Parameters", requestHandler, ParameterSource.HEADER);
        writeParameterInfos(page.head(),requestPanel, "Cookie Parameters", requestHandler, ParameterSource.COOKIE);

        ParameterInfo contentParameterInfo = findContentParameter(requestHandler);
        if (contentParameterInfo != null)
        {
            Panel2 contentParameterPanel=requestPanel.content().returnAddInner(new Panel2(page.head(),"Content Parameter"));

            Parameter contentParameter = method.getParameters()[contentParameterInfo.getIndex()];
            ParameterWriter parameterWriter = new ParameterWriter(page.head(),contentParameterPanel);
            parameterWriter.write(contentParameter.getType());

            if (requestHandler.getContentReaders().size() > 0)
            {
                requestPanel.content().addInner(new p());
                Panel2 contentReaderPanel=requestPanel.content().returnAddInner(new Panel2(page.head(),"Content Readers"));
                for (ContentReader<?> contentReader : requestHandler.getContentReaders().values())
                {
                    WideTable table=contentReaderPanel.content().returnAddInner(new WideTable());
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
                        textarea area=accordion.panel().returnAddInner(new textarea().readonly().style("width:100%;").rows(Utils.occurs(schema, "\r")+1));
                        area.addInner(schema);
                    }
                    if (example.length() > 0)
                    {
                        Accordion accordion=contentReaderPanel.content().returnAddInner(new Accordion(page.head(), null, false, "Example"));
                        textarea area=accordion.panel().returnAddInner(new textarea().readonly().style("width:100%;").rows(Utils.occurs(example,"\r")+1));
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
            Panel1 responsePanel=page.content().returnAddInner(new Panel1(page.head(), "Response"));
            
            Panel2 returnParameterPanel=responsePanel.content().returnAddInner(new Panel2(page.head(),"Return Type"));
            
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
                Panel2 contentWriterPanel=responsePanel.content().returnAddInner(new Panel2(page.head(),"Content Writers"));
                for (ContentWriterList list : lists.values())
                {
                    WideTable table=contentWriterPanel.content().returnAddInner(new WideTable());
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
                            textarea area=accordion.panel().returnAddInner(new textarea().readonly().style("width:100%;").rows(Utils.occurs(schema, "\r")+1));
                            area.addInner(schema);
                        }
                        if (example.length() > 0)
                        {
                            Accordion accordion=contentWriterPanel.content().returnAddInner(new Accordion(page.head(), null, false, "Example"));
                            textarea area=accordion.panel().returnAddInner(new textarea().readonly().style("width:100%;").rows(Utils.occurs(example,"\r")+1));
                            area.addInner(example);
                        }
                    }
                    contentWriterPanel.content().addInner(new p());
                }
            }
        }

        if (requestHandler.getContentDecoders().size() > 0)
        {
            Panel1 panel=page.content().returnAddInner(new Panel1(page.head(),"Content Decoders"));
            WideTable table=panel.content().returnAddInner(new WideTable());
            table.setHeadRow(new Row().add("Class","Content-Encoding","Encoder"));
            for (Entry<String, ContentDecoder> entry : requestHandler.getContentDecoders().entrySet())
            {
                table.addBodyRow(new Row().add(entry.getValue().getClass().getName(),entry.getValue().getCoding(),entry.getKey()));
            }
        }
        if (requestHandler.getContentEncoders().size() > 0)
        {
            Panel1 panel=page.content().returnAddInner(new Panel1(page.head(),"Content Encoders"));
            WideTable table=panel.content().returnAddInner(new WideTable());
            table.setHeadRow(new Row().add("Class","Content-Encoding","Encoder"));
            for (Entry<String, ContentEncoder> entry : requestHandler.getContentEncoders().entrySet())
            {
                table.addBodyRow(new Row().add(entry.getValue().getClass().getName(),entry.getValue().getCoding(),entry.getKey()));
            }
        }
        if (requestHandler.getFilters().length > 0)
        {
            Panel1 panel=page.content().returnAddInner(new Panel1(page.head(),"Filters"));
            WideTable table=panel.content().returnAddInner(new WideTable());
            Row row=new Row();
            for (Filter filter : requestHandler.getFilters())
            {
                row.add(filter.getClass().getName());
            }
            table.addBodyRow(row);
        }
        Panel1 methodPanel=page.content().returnAddInner(new Panel1(page.head(),"Class Method"));
        methodPanel.content().addInner(Utils.escapeHtml(method.toGenericString()+";"));

        
        Panel1 executePanel=page.content().returnAddInner(new Panel1(page.head(),"Execute: "+key));
        String httpMethod = requestHandler.getHttpMethod();
        {
            fieldset fieldset=executePanel.content().returnAddInner(new fieldset());
            fieldset.addInner(new legend().addInner("Parameters"));
            AjaxButton button = new AjaxButton("button", "Execute", "/operator/httpServer/method/execute/"+server);
            button.parameter("key", key);
            writeInputParameterInfos(page.head(),fieldset, button, "Query Parameters", requestHandler, ParameterSource.QUERY);
            writeInputParameterInfos(page.head(),fieldset, button, "Path Parameters", requestHandler, ParameterSource.PATH);
            writeInputParameterInfos(page.head(),fieldset, button, "Header Parameters", requestHandler, ParameterSource.HEADER);
            writeInputParameterInfos(page.head(),fieldset, button, "Cookie Parameters", requestHandler, ParameterSource.COOKIE);
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
                            Panel3 panel3=fieldset.returnAddInner(new Panel3(page.head(),"Request Content"));
                            textarea input=panel3.content().returnAddInner(new textarea()).id("contentText").style("width:100%;height:10em;");
                            button.val("contentText", "contentText");
                        }
                    }
                }
            }
            NameValueList list=fieldset.returnAddInner(new NameValueList(": "));
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
                    options.id("accept").style("width:100%;");
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
                options.id("contentType").style("width:100%;");
                for (String type:set)
                {
                    options.add(type);
                }
                list.add("ContentType", options);
                button.val("contentType", "contentType");
            }
            fieldset.addInner(new p());
            list.add("Additional Headers",new input_text().id("headers").style("width:100%;"));
            button.val("headers", "headers");
            
            fieldset.addInner(new p());
            fieldset.addInner(button);
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
        HttpServletRequest request = context.getHttpServletRequest();
        return new HttpClientEndPoint(HttpClientFactory.createDefaultClient(),endPoint);
    }
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
                Panel2 panel=new Panel2(null, "Not implemented");
                panel.content().addInner("Method=" + method);
                result.put("result",panel.toString());
                return result;
            }
            AjaxQueryResult result = new AjaxQueryResult();
            Panel2 resultPanel=new Panel2(null, "Result");
            Panel3 statusPanel=resultPanel.content().returnAddInner(new Panel3(null, "Performance and Status"));
            NameValueList list=statusPanel.content().returnAddInner(new NameValueList(": "));
            list.add("Time",Utils.millisToLocalDateTimeString(System.currentTimeMillis()));
            list.add("Duration", duration * 1000 + " ms");
            list.add("Status Code",statusCode);
            if (response!=null)
            {
                if (response.getHeaders().length > 0)
                {
                    resultPanel.content().addInner(new p());
                    Panel3 panel=resultPanel.content().returnAddInner(new Panel3(null, "Response Headers"));
                    NameValueList headerList=panel.content().returnAddInner(new NameValueList(": "));
                    for (Header header : response.getHeaders())
                    {
                        headerList.add(header.getName(),header.getValue());
                    }
                }
                if (response.getText().length() > 0)
                {
                    resultPanel.content().addInner(new p());
                    Panel3 contentPanel=resultPanel.content().returnAddInner(new Panel3(null,"Content"));
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
            Panel2 panel=new Panel2(null, "Internal Execution Exception");
            String text=Utils.getStrackTraceAsString(t);
            textarea area=panel.content().returnAddInner(new textarea());
            area.readonly().style("width:100%;").rows(Utils.occurs(text, "\r")+1);
            area.addInner(text);
            result.put("result", panel.toString());
            return result;
            
        }
    }

    @GET
    @Path("/")
    public Element main() throws Throwable
    {
        OperatorPage page=this.serverApplication.buildOperatorPage("Main");
        long now = System.currentTimeMillis();
        page.content().returnAddInner(new NameValueList(": "))
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
                    page.content().addInner(new p());
                    Panel panel=page.content().returnAddInner(new Panel(page.head(),null,"Logger Worker Stats"));
                    Table table=panel.content().returnAddInner(new Table());
                    table.addInner(new style().addInner("thead {background-color:#eee;} td {border:1px solid #888;padding:4px;} table{border-collapse:collapse;} "));
                    table.thead().style("background-color:#eee");
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
                    page.content().addInner(new p());
                    Panel panel=page.content().returnAddInner(new Panel(page.head(),null,"Logger Performance"));
                    Table table=panel.content().returnAddInner(new Table());
                    table.addInner(new style().addInner("thead {background-color:#eee;} td {border:1px solid #888;padding:4px;} table{border-collapse:collapse;} "));
                    table.thead().style("background-color:#eee");
                    table.setHeadRowItems("","Bytes","KB","MB","GB");
                
                    writeSize(table, "Write Rate (per second)", sink.getWriteRateMeter().sampleRate(this.rateSamplingDuration));
                    writeSize(table, "Written", sink.getWriteRateMeter().getCount());
                }
            }
            
            {
                LogDirectoryInfo info = manager.getLogDirectoryInfo();
                page.content().addInner(new p());
                Panel panel=page.content().returnAddInner(new Panel(page.head(),null,"Volume Info"));
                Table table=panel.content().returnAddInner(new Table());
                table.addInner(new style().addInner("thead {background-color:#eee;} td {border:1px solid #888;padding:4px;} table{border-collapse:collapse;} "));
                table.addBodyRowItems("Path",manager.getFullDirectoryPath());
                table.addBodyRowItems("Deletes due to directory size exceeded",manager.getDirectorySizeDeleteMeter().getCount());
                table.addBodyRowItems("Deletes due to maximum files exceeded",manager.getMaximumFilesDeleteMeter().getCount());
                table.addBodyRowItems("Deletes due to reserve space exceeded",manager.getReserveSpaceDeleteMeter().getCount());
                table.addBodyRowItems("File delete failures",manager.getFileDeleteFailedMeter().getCount());
                table.addBodyRowItems("Number of files",info.getFileCount());
                table.addBodyRowItems("Oldest file name",info.getOldestFileName());
                table.addBodyRowItems("Oldest file date",Utils.millisToLocalDateTimeString(info.getOldestFileDate()));
                table.addBodyRowItems("Newest file name",info.getNewestFileName());
                table.addBodyRowItems("Newest file date",Utils.millisToLocalDateTimeString(info.getNewestFileDate()));
            }
            
            LogDirectoryInfo info = manager.getLogDirectoryInfo();
            {
                page.content().addInner(new p());
                Panel panel=page.content().returnAddInner(new Panel(page.head(),null,"Volume Usage"));
                Table table=panel.content().returnAddInner(new Table());
                table.addInner(new style().addInner("thead {background-color:#eee;} td {border:1px solid #888;padding:4px;} table{border-collapse:collapse;} "));
                table.thead().style("background-color:#eee");
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
            }
            if (info.getThrowable() != null)
            {
                page.content().addInner(new p());
                Panel panel=page.content().returnAddInner(new Panel(page.head(),null,"Volume Exception"));
                panel.content().addInner(Utils.toString(info.getThrowable()));
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
