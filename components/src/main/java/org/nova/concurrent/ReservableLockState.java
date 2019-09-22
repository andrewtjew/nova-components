package org.nova.concurrent;

public class ReservableLockState implements AutoCloseable
{
    ReservableLock lock;
    
    ReservableLockState(ReservableLock lock)
    {
        this.lock=lock;
    }
    
    @Override
    public void close() throws Exception
    {
        this.lock.unlock();
        
    }
    
}
