package org.nova.metrics;

import org.nova.collections.RingBuffer;

public class RecentSourceEventMeter
{
    final RingBuffer<SourceEvent> events;
    private long count;
    public RecentSourceEventMeter(int eventBuferSize)
    {
        this.events=new RingBuffer<>(new SourceEvent[eventBuferSize]);
    }
    public void update(Object state,int stackTraceStartIndex)
    {
        synchronized(this)
        {
            this.events.add(new SourceEvent(state,Thread.currentThread().getStackTrace(),stackTraceStartIndex+3));
            this.count++;
        }
    }
    public void update(Object state)
    {
        update(state,3);
    }
    public RecentSourceEventSample sample()
    {
        synchronized(this)
        {
            return new RecentSourceEventSample(events.getSnapshot(), this.count);
        }
    }
    
    
}
