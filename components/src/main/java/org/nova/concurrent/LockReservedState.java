package org.nova.concurrent;

import org.nova.tracing.Trace;

public class LockReservedState implements AutoCloseable
{
    final private ReservableLock lock;
    final private boolean open;
    final private Trace trace;

    LockReservedState(ReservableLock lock,boolean open,Trace trace)
    {
        this.lock=lock;
        this.open=open;
        this.trace=trace;
    }
    public boolean isOpen()
    {
        return this.open;
    }
    
    public ReservableLockState lock(Object object) throws Exception
    {
        if (object==null)
        {
            throw new Exception("object==null");
        }
        if (this.open==false)
        {
            throw new Exception("no reservation");
        }
        this.lock.setLockObject(object);
        return new ReservableLockState(this.lock);
    }
    @Override
    public void close() throws Exception
    {
        this.lock.releaseReservation();
        this.trace.close();
    }
}
