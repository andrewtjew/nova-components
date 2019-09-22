package org.nova.services;
import org.eclipse.jetty.http.HttpStatus;
import org.nova.annotations.Description;
import org.nova.concurrent.TimeBase;
import org.nova.concurrent.TimerTask;
import org.nova.frameworks.ServerApplication;
import org.nova.frameworks.ServerApplicationUtils;
import org.nova.http.client.JSONClient;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.Response;
import org.nova.http.server.HtmlContentWriter;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.Path;
import org.nova.logging.Item;
import org.nova.logging.Level;
import org.nova.metrics.CountMeter;
import org.nova.tracing.Trace;
import org.nova.utils.Utils;

@Description("Handlers for server operator pages")
@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({ JSONContentReader.class, JSONPatchContentReader.class })
@ContentWriters({HtmlContentWriter.class})

public class ServerStatusController
{
    public final ServerApplication serverApplication;
    private int statusCode; 
    final private long interval;
    final private CountMeter checkMeter;
    final private JSONClient client;
    public ServerStatusController(ServerApplication serverApplication) throws Throwable
    {
        this.serverApplication = serverApplication;
        setStatusCode(HttpStatus.OK_200);
        this.checkMeter=new CountMeter();
        this.interval=this.serverApplication.getConfiguration().getLongValue("Application.Monitoring.checkInterval",10*1000);
        
        this.client=ServerApplicationUtils.createJSONClient(this.serverApplication, "Application.Monitoring.serviceEndPointConfiguration");
        if (this.client!=null)
        {
            this.serverApplication.getTimerScheduler().schedule("RegisterWithRemoteServerMonitor", TimeBase.FREE, this.interval/2,this.interval,(trace,event)->{register(trace,event);});
        }
        
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
            Item item=new Item(this.getClass().getSimpleName()+".registration","Success");
            this.serverApplication.getLogger().log(Level.NOTICE,"Monitoring",item);
        }
    }
    
}
