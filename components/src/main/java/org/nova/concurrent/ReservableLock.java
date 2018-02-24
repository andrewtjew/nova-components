package org.nova.concurrent;

import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class ReservableLock
{
    private long delayInstantMs;
    private Object object;
    private boolean reserved;
    private final long lockDelayMs;
    private final TraceManager traceManager;
    
    public ReservableLock(TraceManager traceManager,long lockDelayMs)
    {
        this.object=null;
        this.traceManager=traceManager;
        this.lockDelayMs=lockDelayMs;
        this.delayInstantMs=0;
    }
    
    public boolean isOpen()
    {
        synchronized (this)
        {
            if (this.reserved||this.object!=null)
            {
                return false;
            }
            if (this.lockDelayMs>0)
            {
                if (System.currentTimeMillis()-this.delayInstantMs<=this.lockDelayMs)
                {
                    return false;
                }
            }
            return true;
        }        
    }
    public LockReservation openReservation(Trace parent,String categoryOverride)
    {
        if (categoryOverride==null)
        {
            categoryOverride=this.getClass().getSimpleName();
        }
        Trace trace=new Trace(this.traceManager,parent,categoryOverride);
        synchronized (this)
        {
            boolean open=this.reserved==false&&this.object==null;
            if (open&&(this.lockDelayMs>0))
            {
                if (System.currentTimeMillis()-this.delayInstantMs<=this.lockDelayMs)
                {
                    open=false;
                }
            }
            LockReservation context=new LockReservation(this, open, trace);
            this.reserved=true;
            return context;
        }        
    }
    
    public boolean waitForUnlock(long timeout)
    {
        synchronized(this)
        {
            return Synchronization.waitForNoThrow(this, ()->{return this.object==null;},timeout);
        }        
    }

    public void waitForUnlock()
    {
        synchronized(this)
        {
            Synchronization.waitForNoThrow(this, ()->{return this.object==null;});
        }        
    }

    
    void releaseReservation()
    {
        synchronized(this)
        {
            this.reserved=false;
        }
    }
    
    void setObject(Object object)
    {
        synchronized(this)
        {
            this.object=object;
        }
    }
    
    public Object getLockObject()
    {
        synchronized(this)
        {
            return this.object;
        }
    }

    @SuppressWarnings("unchecked")
    public <OBJECT> OBJECT getLockObject(Class<OBJECT> type)
    {
        synchronized(this)
        {
            if ((this.object==null)||(this.object.getClass()!=type))
            {
                return null;
            }
            return (OBJECT)this.object;
        }
    }

    public <OBJECT> OBJECT unlock(Class<OBJECT> type)
    {
        synchronized(this)
        {
            if (this.object==null)
            {
                return null;
            }
            
            @SuppressWarnings("unchecked")
            OBJECT object=(this.object.getClass()==type)?(OBJECT)this.object:null;
            this.object=null;
            if (this.lockDelayMs>0)
            {
                this.delayInstantMs=System.currentTimeMillis();
            }
            return object;
        }
    }
    
    public void delayLock()
    {
        synchronized(this)
        {
            this.delayInstantMs=System.currentTimeMillis();
        }
    }
    
    public void unlock()
    {
        synchronized(this)
        {
            this.object=null;
        }
    }
    
}
