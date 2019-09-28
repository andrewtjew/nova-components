package org.nova.services;


import org.nova.frameworks.CoreEnvironment;
import org.nova.frameworks.ServerApplication;
import org.nova.http.server.Context;
import org.nova.http.server.HttpServer;
import org.nova.http.server.Response;
import org.nova.tracing.Trace;
import org.nova.utils.Utils;

public abstract class SessionServerApplication<SESSION extends Session> extends ServerApplication
{
    final private TokenGenerator tokenGenerator;
    final private SessionManager<SESSION> sessionManager;
    private SessionFilter sessionFilter;

    public SessionServerApplication(String name,CoreEnvironment coreEnvironment,HttpServer operatorServer) throws Throwable
    {
    	this(name,coreEnvironment,operatorServer,new DefaultAbnormalSessionRequestHandler());
    }

    public SessionServerApplication(String name,CoreEnvironment coreEnvironment,HttpServer operatorServer,AbnormalSessionRequestHandling...sessionRejectResponders) throws Throwable
    {
        super(name,coreEnvironment,operatorServer);
        long lockTimeoutMs=this.getConfiguration().getLongValue("SessionServerApplication.session.lockTimeout", 10*1000);
        long timeoutMs=this.getConfiguration().getLongValue("SessionServerApplication.session.timeout", 30*60*1000);
        int generations=this.getConfiguration().getIntegerValue("SessionServerApplication.session.timeoutGenerations", 10);
        this.sessionManager=new SessionManager<SESSION>(this.getTraceManager(),this.getLogger("SessionService"),this.getTimerScheduler(), lockTimeoutMs,timeoutMs, generations);
        this.tokenGenerator=new TokenGenerator();

        String directoryServiceEndPoint=this.getConfiguration().getValue("SessionServerApplication.directoryServiceEndPoint", "http://"+Utils.getLocalHostName()+":"+this.getPublicServer().getPorts()[0]);
        String headerTokenKey=this.getConfiguration().getValue("SessionServerApplication.tokenKey.header", "X-Token");
        String queryTokenKey=this.getConfiguration().getValue("SessionServerApplication.tokenKey.query", "token");
        String cookieTokenKey=this.getConfiguration().getValue("SessionServerApplication.tokenKey.cookie", headerTokenKey);
        this.sessionFilter=new SessionFilter(this.sessionManager,headerTokenKey,queryTokenKey,cookieTokenKey,sessionRejectResponders);

        this.getMenuBar().add("/operator/sessions","Sessions","View All");
        this.getPublicServer().addFilters(this.sessionFilter);
        SessionOperatorPages<SESSION> sessionOperatorPages=new SessionOperatorPages<>(this.sessionManager,this);
        this.getOperatorServer().registerHandlers(sessionOperatorPages);
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
        String token=this.tokenGenerator.next();
        while (this.sessionManager.getSessionByToken(token)!=null)
        {
            token=this.tokenGenerator.next();
        }
        return token;
    }
    
    public TokenGenerator getTokenGenerator()
    {
        return this.tokenGenerator;
    }

    public String generateToken()
    {
        return this.tokenGenerator.next();
    }
    
}
