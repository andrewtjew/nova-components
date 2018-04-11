package org.nova.metrics;

import org.nova.collections.RingBuffer;
import org.nova.tracing.Trace;

public class TraceHistoryMeter
{
    final private RingBuffer<Trace> traces;
    final private RingBuffer<Trace> exceptionTraces;
    
    private Trace firstRecentChainExceptionTrace;
    private long recentChainExceptionTraces;
    private long lastChainNormalTraces;
    
    public TraceHistoryMeter(int capacity)
    {
        this.traces=new RingBuffer<>(new Trace[capacity]);
        this.exceptionTraces=new RingBuffer<>(new Trace[capacity]);
    }
    public void update(Trace trace)
    {
        synchronized (this)
        {
            this.traces.add(trace);
            if (trace.getThrowable()!=null)
            {
                this.exceptionTraces.add(trace);
                if (this.lastChainNormalTraces!=0)
                {
                    this.lastChainNormalTraces=0;
                    this.firstRecentChainExceptionTrace=trace;
                    this.recentChainExceptionTraces=0;
                }
                this.recentChainExceptionTraces++;
            }
            else
            {
                this.lastChainNormalTraces++;
            }
        }
    }
    
    public TraceHistorySample sample()
    {
        synchronized (this)
        {
            return new TraceHistorySample(this.traces.getSnapshot(), this.exceptionTraces.getSnapshot(), this.firstRecentChainExceptionTrace, this.recentChainExceptionTraces, this.lastChainNormalTraces);
        }
    }
}
