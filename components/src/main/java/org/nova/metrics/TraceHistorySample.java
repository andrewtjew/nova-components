package org.nova.metrics;

import java.util.List;

import org.nova.collections.RingBuffer;
import org.nova.tracing.Trace;

public class TraceHistorySample
{
    final private List<Trace> traces;
    final private List<Trace> exceptionTraces;
    
    final private Trace firstRecentChainExceptionTrace;
    final private long recentChainExceptionTraces;
    final private long lastChainNormalTraces;
    
    public TraceHistorySample(List<Trace> traces,List<Trace> exceptionTraces,Trace firstRecentChainExceptionTrace,long recentChainExceptionTraces,long lastChainNormalTraces)
    {
        this.traces=traces;
        this.exceptionTraces=exceptionTraces;
        this.firstRecentChainExceptionTrace=firstRecentChainExceptionTrace;
        this.recentChainExceptionTraces=recentChainExceptionTraces;
        this.lastChainNormalTraces=lastChainNormalTraces;
    }

    public List<Trace> getTraces()
    {
        return traces;
    }

    public List<Trace> getExceptionTraces()
    {
        return exceptionTraces;
    }

    public Trace getFirstRecentChainExceptionTrace()
    {
        return firstRecentChainExceptionTrace;
    }

    public long getRecentChainExceptionTraces()
    {
        return recentChainExceptionTraces;
    }

    public long getLastChainNormalTraces()
    {
        return lastChainNormalTraces;
    }
    
    
}
