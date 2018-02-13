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
    private static String getSource(StackTraceElement[] elements,int index)
    {
        if (index>=elements.length)
        {
            return null;
        }
        StackTraceElement element=elements[index];
        return element.getFileName()+"."+element.getLineNumber();
    }
    public void update(Object state,int sourceIndex)
    {
        synchronized(this)
        {
            this.events.add(new SourceEvent(state,getSource(Thread.currentThread().getStackTrace(),sourceIndex+3)));
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
