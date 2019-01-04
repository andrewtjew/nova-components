package org.nova.http.server;

public class LatencyTracker
{
    private LatencyDescriptor latencyDescriptor;
    private long updateCount;
    private long totalBeforeExecuteLatencyMs;
    private long totalAfterExecuteLatencyMs;
    
    public LatencyTracker(LatencyDescriptor latencyDescriptor)
    {
        this.latencyDescriptor=latencyDescriptor;
    }

    public void update(long beforeExecuteMs,long afterExecuteMs)
    {
        synchronized(this)
        {
            this.updateCount++;
            if (beforeExecuteMs>0)
            {
                this.totalBeforeExecuteLatencyMs+=beforeExecuteMs;
            }
            if (afterExecuteMs>0)
            {
                this.totalAfterExecuteLatencyMs+=afterExecuteMs;
            }
        }
    }
    
    public LatencyDescriptor getLatencyDescriptor()
    {
        return this.latencyDescriptor;
    }
    
    public void setLatencyDescriptor(LatencyDescriptor latencyDescriptor)
    {
        this.latencyDescriptor=latencyDescriptor;
    }
    
    public LatencyStatistics getLatencyStatistics()
    {
        synchronized(this)
        {
            return new LatencyStatistics(this.updateCount, this.totalBeforeExecuteLatencyMs, this.totalAfterExecuteLatencyMs);
        }
    }
    
}