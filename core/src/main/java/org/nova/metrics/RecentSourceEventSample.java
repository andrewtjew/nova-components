package org.nova.metrics;

import java.util.List;

public class RecentSourceEventSample
{
    final private long createdMs;
    final List<SourceEvent> events;
    final long count;
    
    RecentSourceEventSample(List<SourceEvent> events,long count)
    {
        this.events=events;
        this.createdMs=System.currentTimeMillis();
        this.count=count;
    }

    public long getCreatedMs()
    {
        return createdMs;
    }

    public List<SourceEvent> getEvents()
    {
        return events;
    }

    public long getCount()
    {
        return count;
    }
    
}
