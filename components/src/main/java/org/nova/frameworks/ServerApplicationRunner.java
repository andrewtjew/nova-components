package org.nova.frameworks;

import org.nova.configuration.Configuration;
import org.nova.configuration.ConfigurationReader;
import org.nova.core.Utils;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.widgets.AjaxQueryResultWriter;
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


public class ServerApplicationRunner //
{
    public static interface ServerApplicationInstantiator
    {
        ServerApplication newServerApplication(CoreEnvironment coreEnvironment,HttpServer operatorServer) throws Throwable;
    }
    
    private Throwable startupException;
    
    private HttpServer startOperatorServer(Configuration configuration,TraceManager traceManager,Logger logger) throws Exception
    {
        int threads=configuration.getIntegerValue("HttpServer.operator.threads",10);
        int operatorPort=configuration.getIntegerValue("HttpServer.operator.port",10051);
        boolean test=configuration.getBooleanValue("System.test",false);
        HttpServer operatorServer=new HttpServer(traceManager,logger, test,JettyServerFactory.createServer(threads, operatorPort));
        operatorServer.addContentDecoders(new GzipContentDecoder());
        operatorServer.addContentEncoders(new GzipContentEncoder());
        operatorServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
        operatorServer.addContentWriters(new HtmlContentWriter(),new HtmlElementWriter(),new JSONContentWriter(),new AjaxQueryResultWriter());
        operatorServer.start();
        System.out.println("http://"+Utils.getLocalHostName()+":"+operatorPort);
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
    
    
    public void run(String[] args,ServerApplicationInstantiator instantiator)
    {
        Configuration configuration=ConfigurationReader.search(args);
        if (configuration==null)
        {
            System.err.println("Cannot locate configuration files.");
            return;
        }
        CoreEnvironment coreEnvironment=null;
        try
        {
            System.out.println("Starting CoreEnvironment...");
            coreEnvironment=new CoreEnvironment(configuration);
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
            return;
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
            return;
        }
        
        ServerApplication serverApplication=null;
        try
        {
            showNotice(coreEnvironment.getLogger(),"New ServerApplication...");
            serverApplication=instantiator.newServerApplication(coreEnvironment,operatorServer);
        }
        catch (Throwable t)
        {
            showException(coreEnvironment.getLogger(),t);
            return;
        }

        try
        {
            showNotice(coreEnvironment.getLogger(),"Start ServerApplication...");
            serverApplication.start();
        }
        catch (Throwable t)
        {
            showException(coreEnvironment.getLogger(),t);
            return;
        }
        showNotice(coreEnvironment.getLogger(),"Started");
        try (Trace trace=new Trace(coreEnvironment.getTraceManager(),"run"))
        {
            serverApplication.run(trace);
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
            context.getHttpServletResponse().getWriter().write("No exception");
        }
    }
}
