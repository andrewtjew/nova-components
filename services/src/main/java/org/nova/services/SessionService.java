package org.nova.services;


import org.nova.configuration.Configuration;
import org.nova.core.Utils;
import org.nova.frameworks.ServerApplication;

public class SessionService<SESSION extends Session> extends ServerApplication
{
    final private SessionManager<SESSION> sessionManager;
    final private TokenGenerator tokenGenerator;
    final private SessionFilter sessionFilter;
    public SessionService(String name,Configuration configuration,SessionRejectResponder...sessionRejectResponders) throws Throwable
    {
        super(name,configuration);
        long lockTimeoutMs=this.getConfiguration().getLongValue("SessionService.session.lockTimeout", 1*1000);
        long timeoutMs=this.getConfiguration().getLongValue("SessionService.session.timeout", 30*60*1000);
        int generations=this.getConfiguration().getIntegerValue("SessionService.session.timeoutGenerations", 10);
        this.sessionManager=new SessionManager<SESSION>(this.getTraceManager(),this.getLogger("SessionService"),this.getTimerScheduler(), lockTimeoutMs,timeoutMs, generations);
        this.getMeterManager().register("SessionManager", this.sessionManager);
        this.tokenGenerator=new TokenGenerator();
        
        String directoryServiceEndPoint=this.getConfiguration().getValue("SessionService.directoryServiceEndPoint", "http://"+Utils.getLocalHostName()+":"+this.getPublicServer().getPreferredPort());
        String headerTokenKey=this.getConfiguration().getValue("SessionService.tokenKey.header", "X-Token");
        String queryTokenKey=this.getConfiguration().getValue("SessionService.tokenKey.query", "token");
        String cookieTokenKey=this.getConfiguration().getValue("SessionService.tokenKey.cookie", headerTokenKey);
        this.sessionFilter=new SessionFilter(this.sessionManager,directoryServiceEndPoint,headerTokenKey,queryTokenKey,cookieTokenKey,sessionRejectResponders);
    }

    @Override
    public void onStart() throws Throwable
    {
        super.onStart();
        this.getOperationContentWriter().getMenu().add("Sessions|View All", "/operator/sessions");
        
        this.getPublicServer().addFilters(this.sessionFilter);
        SessionOperatorPages<SESSION> sessionOperatorPages=new SessionOperatorPages<>(this.sessionManager);
        this.getOperatorServer().register(sessionOperatorPages);
    }
    public SessionManager<SESSION> getSessionManager()
    {
        return this.sessionManager;
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
