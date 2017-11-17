package org.nova.frameworks;

import org.nova.configuration.Configuration;
import org.nova.configuration.ConfigurationReader;
import org.nova.core.Utils;
import org.nova.frameworks.ServerApplicationPages.Level1Panel;
import org.nova.frameworks.ServerApplicationPages.Level2Panel;
import org.nova.frameworks.ServerApplicationPages.WideTable;
import org.nova.html.elements.Element;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.tags.p;
import org.nova.html.tags.style;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.html.widgets.NameValueList;
import org.nova.html.widgets.Panel;
import org.nova.html.widgets.Table;
import org.nova.html.widgets.Text;
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
import org.nova.logging.HighPerformanceLogger;
import org.nova.logging.Level;
import org.nova.logging.LogDirectoryInfo;
import org.nova.logging.LogDirectoryManager;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;


public class CoreEnvironmentApplicationRunner //
{
    public static interface CoreEnvironmentApplicationInstantiator
    {
        CoreEnvironmentApplication instantiate(CoreEnvironment coreEnvironment) throws Throwable;
    }
    
    
    private void showNotice(Logger logger,String message)
    {
        System.out.println(message);
        logger.log(Level.NOTICE, message);
    }
    
    private void showException(Logger logger,Throwable t)
    {
        t.printStackTrace(System.err);
        logger.log(t);
    }
    
    
    public void run(String[] args,CoreEnvironmentApplicationInstantiator instantiator)
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
        
        CoreEnvironmentApplication application=null;
        try
        {
            showNotice(coreEnvironment.getLogger(),"New Application...");
            application=instantiator.instantiate(coreEnvironment);
        }
        catch (Throwable t)
        {
            showException(coreEnvironment.getLogger(),t);
            return;
        }

        try (Trace trace=new Trace(coreEnvironment.getTraceManager(),"run"))
        {
            try
            {
                application.run(trace);
            }
            catch (Throwable t)
            {
                showException(coreEnvironment.getLogger(),t);
            }
            finally
            {
                coreEnvironment.stop();
            }
        }
    }

}
