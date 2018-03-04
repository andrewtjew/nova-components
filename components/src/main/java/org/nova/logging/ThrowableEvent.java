package org.nova.logging;

public class ThrowableEvent
{
    final Throwable throwable;
    final long instantMs;
    
    ThrowableEvent(Throwable throwable)
    {
        this.throwable=throwable;
        this.instantMs=System.currentTimeMillis();
    }

    public Throwable getThrowable()
    {
        return throwable;
    }

    public long getInstantMs()
    {
        return instantMs;
    }
}
