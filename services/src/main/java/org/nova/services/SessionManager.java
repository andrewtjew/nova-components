package org.nova.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.nova.annotations.Description;
import org.nova.collections.Expire;
import org.nova.collections.ExpireMap;
import org.nova.concurrent.Lock;
import org.nova.concurrent.LockManager;
import org.nova.concurrent.TimerScheduler;
import org.nova.logging.Logger;
import org.nova.metrics.CountMeter;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class SessionManager<SESSION extends Session> 
{
    final private ExpireMap<String, SESSION> tokenSessions;
    final private HashMap<String,SESSION> userSessions;
    final private LockManager<String> lockManager;
    final private long waitForLockTimeoutMs;
    final private Logger logger;
    
    @Description("Counts how many times onClose throws exceptions. The exceptions are in the logs.")
    final CountMeter onCloseFailMeter;
    
    @Description("Counts how many times sessions are created.")
    final CountMeter addSessionMeter;

    @Description("Counts how many times sessions are removed. If there are zero active sessions, this value should be the same as the addSessionMeter value.")
    final CountMeter removeSessionMeter;

    @Description("Counts how many session timeout occurred.")
    final CountMeter timeoutSessionMeter;
    
    public SessionManager(TraceManager traceManager,Logger logger,TimerScheduler timerScheduler,long waitForLockTimeoutMs,long sessionTimeoutMs,int generations) throws Exception
    {
        this.tokenSessions=new ExpireMap<>(this.getClass().getSimpleName(), timerScheduler, sessionTimeoutMs, generations,(Trace parent,String key,SESSION session)->{timeoutSession(parent,session);});
        this.logger=logger;
        this.userSessions=new HashMap<>();
        this.waitForLockTimeoutMs=waitForLockTimeoutMs;
        this.lockManager=new LockManager<>(traceManager, this.getClass().getSimpleName());
        
        this.onCloseFailMeter=new CountMeter();
        this.addSessionMeter=new CountMeter();
        this.removeSessionMeter=new CountMeter();
        this.timeoutSessionMeter=new CountMeter();
    }
    public void addSession(Trace trace,SESSION session)
    {
        removeSessionByUser(trace, session.getUser());
        synchronized(this)
        {
            this.tokenSessions.put(session.getToken(),session);
            this.userSessions.put(session.getUser(),session);
        }
        this.addSessionMeter.increment();
    }
    public SESSION getSessionByToken(String token)
    {
        synchronized (this)
        {
            return this.tokenSessions.update(token);
        }
    }
    public SESSION getSessionByUser(String user)
    {
        synchronized (this)
        {
            return this.userSessions.get(user);
        }
    }
    
    
    public boolean removeSessionByToken(Trace trace,String token)
    {
        return removeSession(trace,getSessionByToken(token));
    }
    public boolean removeSessionByUser(Trace trace,String user)
    {
        return removeSession(trace,getSessionByUser(user));
    }
    
    private boolean removeSession(Trace trace,SESSION session)
    {
        if (session==null)
        {
            return false;
        }
        synchronized(this)
        {
            session=userSessions.get(session.getUser());
            if (session==null) //Need to perform this test inside synchronized to ensure onClose is called only once.
            {
                return false;
            }
            this.userSessions.remove(session.getUser());
            this.tokenSessions.remove(session.getToken());
        }
        this.removeSessionMeter.increment();
        try
        {
            session.onClose(trace);
        }
        catch (Throwable t)
        {
            this.logger.log(t);
        }
        return true;
    }
    
    public Collection<SESSION> getSessionSnapshot()
    {
        synchronized (this)
        {
            return this.userSessions.values();
        }
    }
    Lock<String> waitForLock(String user)
    {
        return lockManager.waitForLock(user,this.waitForLockTimeoutMs);
    }
    
    private void timeoutSession(Trace trace, SESSION session) throws Throwable
    {
        this.timeoutSessionMeter.increment();
        removeSession(trace,session);
    }
}
