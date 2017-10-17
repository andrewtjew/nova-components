package org.nova.logging;

public class InstantThrowable
{
    final Throwable throwable;
    final long instantMs;
    
    InstantThrowable(Throwable throwable)
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
