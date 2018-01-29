package org.nova.metrics;

import org.nova.tracing.Trace;

public class LongSizeMeter
{
    long createdMs;
    long totalSize;
    double totalSize2;
    long maxSize;
    long maxSizeInstantMs;
    long minSize;
    long minSizeInstantMs;
    int count;

    public LongSizeMeter()
    {
        this.createdMs=System.currentTimeMillis();
        this.maxSize=Long.MIN_VALUE;
        this.minSize=Long.MAX_VALUE;
    }
    public void update(long size)
    {
        synchronized(this)
        {
            if (size>=this.maxSize)
            {
                this.maxSize=size;
                this.maxSizeInstantMs=System.currentTimeMillis();
            }
            if (size<=this.minSize)
            {
                this.minSize=size;
                this.minSizeInstantMs=System.currentTimeMillis();
            }
            this.totalSize+=size;
            this.totalSize2+=size*size;
            this.count++;
        }
    }
    public LongSizeSample sample()
    {
        synchronized(this)
        {
            long span=System.currentTimeMillis()-this.createdMs;
            if (span<=0)
            {
                span=1;
            }
            double rate=1000.0*this.count/span;
            return new LongSizeSample(this,rate);
        }
    }

}
