package org.nova.http.server;

public class LatencyDescriptor
{
    final private long beforeExecuteMinimumMs;
    final private long beforeExecuteMaximumMs;
    final private long afterExecuteMinimumMs;
    final private long afterExecuteMaximumMs;
    final private boolean enabled;
    
    public LatencyDescriptor(boolean enabled,long beforeExecuteMinimumMs,long beforeExecuteMaximumMs,long afterExecuteMinimumMs,long afterExecuteMaximumMs)
    {
        this.enabled=enabled;
        this.beforeExecuteMinimumMs=beforeExecuteMinimumMs;
        this.beforeExecuteMaximumMs=beforeExecuteMaximumMs;
        this.afterExecuteMinimumMs=afterExecuteMinimumMs;
        this.afterExecuteMaximumMs=afterExecuteMaximumMs;
    }

    public long getBeforeExecuteMinimumMs()
    {
        return beforeExecuteMinimumMs;
    }

    public long getBeforeExecuteMaximumMs()
    {
        return beforeExecuteMaximumMs;
    }

    public long getAfterExecuteMinimumMs()
    {
        return afterExecuteMinimumMs;
    }

    public long getAfterExecuteMaximumMs()
    {
        return afterExecuteMaximumMs;
    }
    
    public boolean isEnabled()
    {
        return this.enabled;
    }
    
}