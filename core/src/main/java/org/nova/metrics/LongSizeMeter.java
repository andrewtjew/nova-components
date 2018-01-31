/*
package org.nova.metrics;

import org.nova.tracing.Trace;

public class LongSizeMeter
{
    private LongSizeSample lastSample;
    long createdMs;

    long markTotalSize;
    double marTotalSize2;
    long markCount;
    int totalCount;
    private long markNs;
    private double minimalResetDurationS;

    long maxSize;
    long maxSizeInstantMs;
    long minSize;
    long minSizeInstantMs;

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
            this.markNs=System.nanoTime();
            this.markTotalSize+=size;
            this.marTotalSize2+=size*size;
            this.totalCount++;
            this.markCount++;
        }
    }
    public void resetExtremas()
    {
        synchronized(this)
        {
            this.maxSize=Long.MIN_VALUE;
            this.minSize=Long.MAX_VALUE;
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
            double rate=1000.0*this.totalCount/span;
            return new LongSizeSample(this,rate);
        }
    }


}
*/