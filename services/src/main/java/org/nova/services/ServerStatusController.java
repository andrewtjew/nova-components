package org.nova.services;
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
import org.eclipse.jetty.server.Server;
import org.nova.annotations.Description;
import org.nova.concurrent.Future;
import org.nova.concurrent.TimerRunnable;
import org.nova.concurrent.TimerTask;
import org.nova.concurrent.TimerTask.TimeBase;
import org.nova.configuration.Configuration;
import org.nova.configuration.ConfigurationItem;
import org.nova.core.Utils;
import org.nova.frameworks.ServerApplication;
import org.nova.html.Attribute;
import org.nova.html.HtmlWriter;
import org.nova.html.Selection;
import org.nova.html.TableList;
import org.nova.html.operator.Menu;
import org.nova.html.operator.OperatorResult;
import org.nova.html.tags.input_submit;
import org.nova.html.widgets.AjaxButton;
import org.nova.html.widgets.AjaxQueryResult;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.http.Header;
import org.nova.http.client.HttpClientConfiguration;
import org.nova.http.client.HttpClientFactory;
import org.nova.http.client.JSONClient;
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
import org.nova.http.server.JettyServerFactory;
import org.nova.http.server.ParameterInfo;
import org.nova.http.server.ParameterSource;
import org.nova.http.server.RequestHandler;
import org.nova.http.server.RequestHandlerNotFoundLogEntry;
import org.nova.http.server.RequestLogEntry;
import org.nova.http.server.Response;
import org.nova.http.server.HtmlContentWriter;
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
@ContentWriters({HtmlContentWriter.class})

public class ServerStatusController
{
    public final ServerApplication serverApplication;
    private int statusCode; 
    final private String monitorEndPoint; 
    final private long interval;
    final private CountMeter checkMeter;
    final private JSONClient client;
    public ServerStatusController(ServerApplication serverApplication) throws Throwable
    {
        this.serverApplication = serverApplication;
        setStatusCode(HttpStatus.OK_200);
        this.monitorEndPoint=serverApplication.getConfiguration().getValue("Application.Monitoring.remoteServerMonitorEndpoint",null);
        this.checkMeter=new CountMeter();
        this.interval=this.serverApplication.getConfiguration().getLongValue("Application.Monitoring.checkInterval",10*1000);
        if (this.monitorEndPoint!=null)
        {
            this.serverApplication.getTimerScheduler().schedule("RegisterWithRemoteServerMonitor", TimeBase.FREE, this.interval/2,this.interval,(trace,event)->{register(trace,event);});
        }
        this.client=new JSONClient(this.serverApplication.getTraceManager(), this.serverApplication.getLogger(), this.monitorEndPoint);
        
    }
    public void setStatusCode(int statusCode)
    {
        this.statusCode=statusCode;
    }
    public int getStatusCode()
    {
        return this.statusCode;
    }
    @GET
    @Path("/check")
    public Response<Void> check(Trace trace) throws Throwable
    {
        this.checkMeter.increment();
        return new Response<Void>(this.statusCode);
    }
    
    static class AddServerRequest
    {
        String url;
        long interval;
        String description;
        AddServerRequest(String url,long interval,String description)
        {
            this.url=url;
            this.interval=interval;
            this.description=description;
        }
    }    
    
    public int raiseAlert(Trace parent,AlertRequest request) throws Throwable
    {
        return this.client.post(parent, null, "/alert",request);
    }

    public int raiseAlert(Trace parent,String type,AlertLevel level,String message,String url) throws Throwable
    {
        return raiseAlert(parent,new AlertRequest(this.serverApplication.getName(),type,level,message,url));
    }
    
    private void register(Trace parent,TimerTask event) throws Throwable
    {
        String localHostName=Utils.getLocalHostName();
        AddServerRequest request=new AddServerRequest("http://"+Utils.getLocalHostName()+":"+this.serverApplication.getPrivateServer().getPorts()[0]+"/check"
                ,this.interval
                ,this.serverApplication.getName()+"@"+localHostName);
                
        if (this.client.post(parent, null, "/server/add",request)<300)
        {
            event.cancel();
        }
    }
    
}
