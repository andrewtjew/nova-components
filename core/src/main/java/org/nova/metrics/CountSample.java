package org.nova.metrics;

public class CountSample
{
    final private long count;
    final private long createdMs;
    CountSample(long count)
    {
        this.count=count;
        this.createdMs=System.currentTimeMillis();
    }
    public long getCount()
    {
        return count;
    }
    public long getCreatedMs()
    {
        return createdMs;
    }
    
}
