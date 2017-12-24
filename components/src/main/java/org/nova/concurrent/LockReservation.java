package org.nova.concurrent;

import org.nova.tracing.Trace;

public class LockReservation implements AutoCloseable
{
    final private ReservableLock lock;
    final private boolean open;
    final private Trace trace;

    LockReservation(ReservableLock lock,boolean open,Trace trace)
    {
        this.lock=lock;
        this.open=open;
        this.trace=trace;
    }
    public boolean isOpen()
    {
        return this.open;
    }
    
    public void lock(Object object) throws Exception
    {
        if (object==null)
        {
            throw new Exception("object==null");
        }
        if (this.open==false)
        {
            throw new Exception("no reservation");
        }
        this.lock.setObject(object);
    }
    @Override
    public void close() throws Exception
    {
        this.lock.releaseReservation();
        this.trace.close();
    }
}
