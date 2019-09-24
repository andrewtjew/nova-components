package org.samples;

import org.nova.frameworks.CoreEnvironment;
import org.nova.frameworks.ServerApplicationRunner;
import org.nova.http.server.HttpServer;
import org.nova.services.SessionServerApplication;
import org.nova.tracing.Trace;

public class Service extends SessionServerApplication<UserSession>
{
    
    public static void main(String[] args) throws Throwable
    {
        new ServerApplicationRunner().run(args,(coreEnvironment,operatorServer)->{return new Service(coreEnvironment,operatorServer);});
    }


    public Service(CoreEnvironment coreEnvironment,HttpServer operatorServer) throws Throwable
    {
        super("Sample", coreEnvironment,operatorServer);
    }
    
    public void onStart(Trace parent) throws Throwable
    {
    	this.getPublicServer().registerHandlers(new OpenController(this));
    	this.getPublicServer().registerHandlers(new SessionController(this));
    }
    
}
