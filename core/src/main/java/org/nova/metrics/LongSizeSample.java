package org.nova.metrics;

import org.nova.tracing.Trace;

public class LongSizeSample
{
    final private  double rate;
    final private long totalSize;
    final private double totalSize2;
    final private long maximumSize;
    final private long maximumSizeInstantMs;
    final private long minimumSize;
    final private long mininumSizeInstantMs;
    final private long count;
    final private long createdMs;

    LongSizeSample(LongSizeMeter meter,double rate)
    {
        this.createdMs=meter.createdMs;
        this.totalSize = meter.totalSize;
        this.totalSize2=meter.totalSize2;
        this.maximumSize = meter.maxSize;
        this.maximumSizeInstantMs = meter.maxSizeInstantMs;
        this.minimumSize = meter.minSize;
        this.mininumSizeInstantMs = meter.minSizeInstantMs;
        this.count = meter.count;
        this.rate=rate;
    }

    public long getCreatedMs()
    {
        return this.createdMs;
    }
    public double getRate()
    {
       return this.rate; 
    }
    public long getMaxSize()
    {
        return this.maximumSize;
    }

    public long getMinimum()
    {
        return this.minimumSize;
    }
    
    public long getMinimumInstantMs()
    {
        return this.mininumSizeInstantMs;
    }

    public long getMaximum()
    {
        return this.maximumSize;
    }

    public long getMaximumInstantMs()
    {
        return this.maximumSizeInstantMs;
    }
    public double getTotalSize()
    {
        return this.totalSize;
    }

    public long getCount()
    {
        return count;
    }
    public double getStandardDeviation()
    {
        double b=((double)this.totalSize/this.count)*this.totalSize;
        double d = this.totalSize2 - b;
        if (d <= 0.0)
        {
            return 0;
        }
        return Math.sqrt(d/this.count);
    }
    public double getAverage()
    {
        return (double)this.totalSize/this.count;
    }
}
