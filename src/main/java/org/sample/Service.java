package org.sample;

import org.nova.configuration.Configuration;
import org.nova.frameworks.CoreEnvironment;
import org.nova.frameworks.ServerApplicationRunner;
import org.nova.http.server.HttpServer;
import org.nova.services.ResourceController;
import org.nova.services.SessionManager;
import org.nova.services.SessionOperatorPages;
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
    SessionManager<UserSession> sessionManager;
    
    public void onStart(Trace parent) throws Throwable
    {
        Configuration configuration=this.getConfiguration();
        
        this.sessionManager=new SessionManager<UserSession>(this.getTraceManager(), this.getLogger(), this.getTimerScheduler(), configuration.getLongValue("Service.session.waitForLockTimeoutMs",3000), configuration.getLongValue("Service.session.timeoutMs",1000L*60L*30L), configuration.getIntegerValue("Service.session.generations",2)); 

    }
    
}
