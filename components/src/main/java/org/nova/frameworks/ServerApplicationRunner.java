package org.nova.frameworks;

//11:47
import org.eclipse.jetty.http.HttpStatus;
import org.nova.configuration.Configuration;
import org.nova.configuration.ConfigurationReader;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.operator.AjaxQueryResultWriter;
import org.nova.http.server.Context;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.HtmlContentWriter;
import org.nova.http.server.HttpServer;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.JettyServerFactory;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.Path;
import org.nova.logging.Level;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;
import org.nova.utils.Utils;


public class ServerApplicationRunner //
{
    public static interface ServerApplicationInstantiator
    {
        ServerApplication instantiate(CoreEnvironment coreEnvironment,HttpServer operatorServer) throws Throwable;
    }

    public static void main(String[] args,ServerApplicationInstantiator instantiator)
    {
        new ServerApplicationRunner().run(args,instantiator);
    }

    
    private Throwable startupException;
    
    private HttpServer startOperatorServer(Configuration configuration,TraceManager traceManager,Logger logger) throws Exception
    {
        int threads=configuration.getIntegerValue("HttpServer.operator.threads",10);
        int operatorPort=configuration.getIntegerValue("HttpServer.operator.port",10051);
        boolean test=configuration.getBooleanValue("System.test",false);
        System.out.println("http://"+Utils.getLocalHostName()+":"+operatorPort);
        HttpServer operatorServer=new HttpServer(traceManager,logger, test,JettyServerFactory.createServer(threads, operatorPort));
        operatorServer.addContentDecoders(new GzipContentDecoder());
        operatorServer.addContentEncoders(new GzipContentEncoder());
        operatorServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
        operatorServer.addContentWriters(new HtmlContentWriter(),new HtmlElementWriter(),new JSONContentWriter(),new AjaxQueryResultWriter());
        operatorServer.start();
        return operatorServer;
    }
    
    private void showNotice(Logger logger,String message)
    {
        System.out.println(message);
        logger.log(Level.NOTICE, message);
    }
    
    private void showException(Logger logger,Throwable t)
    {
        this.startupException=t;
        t.printStackTrace(System.err);
        logger.log(t);
    }
    
    public void run(String[] args,String configurationFileKey,ServerApplicationInstantiator instantiator)
    {
        Configuration configuration=ConfigurationReader.read(args,configurationFileKey);
        if (configuration==null)
        {
            System.err.println("Cannot locate configuration file.");
            return;
        }
        run(configuration,instantiator);
    }
    public void run(String[] args,ServerApplicationInstantiator instantiator)
    {
        run(args,null,instantiator);
    }
    
    public void start(String[] args,String configurationFileKey,ServerApplicationInstantiator instantiator)
    {
        Configuration configuration=ConfigurationReader.read(args,configurationFileKey);
        if (configuration==null)
        {
            System.err.println("Cannot locate configuration files.");
            return;
        }
        start(configuration,instantiator);
    }

    public ServerApplication start(Configuration configuration,ServerApplicationInstantiator instantiator)
    {
        CoreEnvironment coreEnvironment=null;
        try
        {
            System.out.println("Starting CoreEnvironment...");
            coreEnvironment=new CoreEnvironment(configuration);
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
            return null;
        }
        
        HttpServer operatorServer=null;
        try
        {
            showNotice(coreEnvironment.getLogger(),"Starting Operator HttpServer...");
            operatorServer=startOperatorServer(configuration, coreEnvironment.getTraceManager(), coreEnvironment.getLogger("HttpServer.Operator"));
            operatorServer.registerHandlers(this);
        }
        catch (Throwable t)
        {
            showException(coreEnvironment.getLogger(),t);
            return null;
        }
        
        ServerApplication serverApplication=null;
        try
        {
            showNotice(coreEnvironment.getLogger(),"New ServerApplication...");
            serverApplication=instantiator.instantiate(coreEnvironment,operatorServer);
        }
        catch (Throwable t)
        {
            showException(coreEnvironment.getLogger(),t);
            return null;
        }

        try
        {
            showNotice(coreEnvironment.getLogger(),"Start ServerApplication...");
            serverApplication.start();
        }
        catch (Throwable t)
        {
            showException(coreEnvironment.getLogger(),t);
            return null;
        }
        showNotice(coreEnvironment.getLogger(),"Started");
        return serverApplication;
    }
    
    public void run(Configuration configuration,ServerApplicationInstantiator instantiator)
    {
        ServerApplication serverApplication=start(configuration,instantiator);
        try (Trace trace=new Trace(serverApplication.getTraceManager(),"run"))
        {
            serverApplication.join(trace);
        }
    } 

    @GET
    @Path("/operator/exception")
    public void startupException(Context context) throws Throwable
    {
        if (this.startupException!=null)
        {
            this.startupException.printStackTrace(context.getHttpServletResponse().getWriter());
        }
        else
        {
            context.getHttpServletResponse().setHeader("Location","/operator/noStartupExceptions");
            context.getHttpServletResponse().setStatus(HttpStatus.TEMPORARY_REDIRECT_307);
        }
    }
}
