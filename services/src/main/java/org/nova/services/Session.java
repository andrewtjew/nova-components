package org.nova.services;

import org.nova.concurrent.Lock;
import org.nova.core.NameObject;
import org.nova.core.NameValue;
import org.nova.http.server.Context;
import org.nova.metrics.RateMeter;
import org.nova.tracing.Trace;

public abstract class Session
{
    final String token;
    final String user;
    final long created;
    private long lastAccess;
    private Lock<String> lock;
    private RateMeter accessRateMeter;
    
    public Session(String token,String user)
    {
        this.user=user;
        this.token=token;
        this.lastAccess=this.created=System.currentTimeMillis();
        this.accessRateMeter=new RateMeter();
    }
    public void update(Lock<String> lock)
    {
        synchronized(this)
        {
            this.lock=lock;
            this.lastAccess=System.currentTimeMillis();
            this.accessRateMeter.increment();
        }
    }
    public Lock<String> freeLock()
    {
        synchronized (this)
        {
            Lock<String> lock=this.lock;
            this.lock=null;
            return lock;
        }
    }
    public void closeLock()
    {
        synchronized (this)
        {
            if (this.lock!=null)
            {
                this.lock.close();
            }
            this.lock=null;
        }
    }
    public long getLastAccess()
    {
        return lastAccess;
    }
    public String getUser()
    {
        return user;
    }
    public long getCreated()
    {
        return created;
    }
    public String getToken()
    {
        return token;
    }
    public RateMeter getAccessRateMeter()
    {
        return this.accessRateMeter;
    }
    
    abstract public void onClose(Trace trace) throws Throwable;
    abstract public NameObject[] getDisplayItems();
    abstract public boolean isAccessDenied(Trace trace,Context context) throws Throwable;
    
}
