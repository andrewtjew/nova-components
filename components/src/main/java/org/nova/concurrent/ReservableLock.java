package org.nova.concurrent;

import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class ReservableLock
{
    private Object object;
    private boolean reserved;
    private final TraceManager traceManager;
    public ReservableLock(TraceManager traceManager)
    {
        this.object=null;
        this.traceManager=traceManager;
    }
    public LockReservation openReservation(Trace parent)
    {
        return openReservation(parent,null);
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
            LockReservation context=new LockReservation(this, this.reserved==false&&this.object==null, trace);
            this.reserved=true;
            return context;
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
            return object;
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
