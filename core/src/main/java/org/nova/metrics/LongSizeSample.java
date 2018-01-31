/*
package org.nova.metrics;

import org.nova.tracing.Trace;

public class LongSizeSample
{
    LongSizeSample lastSample;
    
    final private long markTotalSize;
    final private double markTotalSize2;
    final private long maximumSize;
    final private long maximumSizeInstantMs;
    final private long minimumSize;
    final private long mininumSizeInstantMs;
    final private long count;
    final private long createdMs;

    LongSizeSample(LongSizeMeter meter,double rate)
    {
        this.createdMs=meter.createdMs;
        this.markTotalSize = meter.markTotalSize;
        this.markTotalSize2=meter.marTotalSize2;
        this.maximumSize = meter.maxSize;
        this.maximumSizeInstantMs = meter.maxSizeInstantMs;
        this.minimumSize = meter.minSize;
        this.mininumSizeInstantMs = meter.minSizeInstantMs;
        this.count = meter.totalCount;
    }

    public long getCreatedMs()
    {
        return this.createdMs;
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
        return this.markTotalSize;
    }

    public long getCount()
    {
        return count;
    }
    public double getStandardDeviation()
    {
        double b=((double)this.markTotalSize/this.count)*this.markTotalSize;
        double d = this.markTotalSize2 - b;
        if (d <= 0.0)
        {
            return 0;
        }
        return Math.sqrt(d/this.count);
    }
    public double getAverage()
    {
        return (double)this.markTotalSize/this.count;
    }
}
*/