package org.nova.frameworks;

import org.nova.configuration.Configuration;
import org.nova.configuration.ConfigurationReader;
import org.nova.logging.Level;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;


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
    
    public void run(Configuration configuration,CoreEnvironmentApplicationInstantiator instantiator)
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
                application.join(trace);
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
    
    public void run(String[] args,CoreEnvironmentApplicationInstantiator instantiator)
    {
        Configuration configuration=ConfigurationReader.read(args,null);
        if (configuration==null)
        {
            System.err.println("Cannot locate configuration files.");
            return;
        }
        run(configuration,instantiator);
    }

}
