package org.nova.services;


import org.nova.configuration.Configuration;
import org.nova.core.Utils;
import org.nova.frameworks.ServerApplication;
import org.nova.tracing.Trace;

public abstract class SessionService<SESSION extends Session> extends ServerApplication
{
    final private TokenGenerator tokenGenerator;
    final private SessionManager<SESSION> sessionManager;
    private SessionFilter sessionFilter;

    public SessionService(String name,Configuration configuration) throws Throwable
    {
        super(name,configuration);
        long lockTimeoutMs=this.getConfiguration().getLongValue("SessionService.session.lockTimeout", 1*1000);
        long timeoutMs=this.getConfiguration().getLongValue("SessionService.session.timeout", 30*60*1000);
        int generations=this.getConfiguration().getIntegerValue("SessionService.session.timeoutGenerations", 10);
        this.sessionManager=new SessionManager<SESSION>(this.getTraceManager(),this.getLogger("SessionService"),this.getTimerScheduler(), lockTimeoutMs,timeoutMs, generations);
        this.getMeterManager().register("SessionManager", this.sessionManager);
        this.tokenGenerator=new TokenGenerator();
        
    }

    public SessionService(String name,Configuration configuration,NoSessionResponder...sessionRejectResponders) throws Throwable
    {
        this(name,configuration);
        String directoryServiceEndPoint=this.getConfiguration().getValue("SessionService.directoryServiceEndPoint", "http://"+Utils.getLocalHostName()+":"+this.getPublicServer().getPorts()[0]);
        String headerTokenKey=this.getConfiguration().getValue("SessionService.tokenKey.header", "X-Token");
        String queryTokenKey=this.getConfiguration().getValue("SessionService.tokenKey.query", "token");
        String cookieTokenKey=this.getConfiguration().getValue("SessionService.tokenKey.cookie", headerTokenKey);
        this.sessionFilter=new SessionFilter(this.sessionManager,directoryServiceEndPoint,headerTokenKey,queryTokenKey,cookieTokenKey,sessionRejectResponders);

        this.getMenuBar().add("/operator/sessions","Sessions","View All");
        this.getPublicServer().addFilters(this.sessionFilter);
        SessionOperatorPages<SESSION> sessionOperatorPages=new SessionOperatorPages<>(this.sessionManager,this);
        this.getOperatorServer().register(sessionOperatorPages);
    }

    public SessionManager<SESSION> getSessionManager()
    {
        return this.sessionManager;
    }

    public void setSessionFilter(SessionFilter sessionFilter)
    {
        this.sessionFilter=sessionFilter;
    }
    
    public SessionFilter getSessionFilter()
    {
        return sessionFilter;
    }

    public String generateSessionToken()
    {
        String token=this.tokenGenerator.generate();
        while (this.sessionManager.getSessionByToken(token)!=null)
        {
            token=this.tokenGenerator.generate();
        }
        return token;
    }
    
}
