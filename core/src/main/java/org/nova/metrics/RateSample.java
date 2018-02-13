package org.nova.metrics;

public class RateSample
{
    RateSample lastSample;
    final private long durationNs;
    final private long samples;
    final private long totalCount;
    final private long createdMs;
    
    public RateSample(RateSample lastSample, long durationNs, long samples,long allTimeCount)
    {
        this.durationNs=durationNs;
        this.samples=samples;
        this.totalCount=allTimeCount;
        this.createdMs=System.currentTimeMillis();
    }

    public long getTotalCount()
    {
        return this.totalCount;
    }
    public RateSample getLastSample()
    {
        return this.lastSample;
    }


    public double getRate()
    {
        if (this.durationNs==0)
        {
            return 0;
        }
        return (1.0e9*samples)/this.durationNs;
    }
    private double blend(double value,double last)
    {
        double weight=(double)this.durationNs/(this.durationNs+this.lastSample.durationNs);
        return weight*value+(1.0-weight)*last;
        
    }
    public double getWeightedRate()
    {
        if (this.lastSample==null)
        {
            return this.getRate();
        }
        return blend(this.getRate(),this.lastSample.getRate());
    }

    public long getSamples()
    {
        return this.samples;
    }
    public long getCreatedMs()
    {
        return this.createdMs;
    }
    public long getSampleDurationNs()
    {
        return this.durationNs;
    }
    public double getSampleDurationS()
    {
        return this.durationNs/1.0e9;
    }

}
