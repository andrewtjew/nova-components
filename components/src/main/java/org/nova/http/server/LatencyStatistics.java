package org.nova.http.server;

public class LatencyStatistics
{
    final private long updateCount;
    final private long totalBeforeExecuteLatencyMs;
    final private long totalAfterExecuteLatencyMs;
    
    public LatencyStatistics(long updateCount,long totalBeforeExecuteLatencyMs,long totalAfterExecuteLatencyMs)
    {
        this.updateCount=updateCount;
        this.totalBeforeExecuteLatencyMs=totalBeforeExecuteLatencyMs;
        this.totalAfterExecuteLatencyMs=totalAfterExecuteLatencyMs;
    }

    public long getUpdateCount()
    {
        return updateCount;
    }

    public long getTotalBeforeExecuteLatencyMs()
    {
        return totalBeforeExecuteLatencyMs;
    }

    public long getTotalAfterExecuteLatencyMs()
    {
        return totalAfterExecuteLatencyMs;
    }
        
    
    
}