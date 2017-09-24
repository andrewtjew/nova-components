package org.nova.metrics;

public class ValueRateSample
{
    ValueRateSample lastSample;
    final private long min;
    final private long minInstantMs;
    final private long max;
    final private long maxInstantMs;
    final private long durationNs;
    final private long count;
    final private long total;
    final private double total2;
    final private long createdMs;
    final private long allTimeTotal;
    final private long allTimeCount;

    public ValueRateSample(ValueRateSample lastSample, long min, long minInstantMs, long max, long maxInstantMs, long durationNs, long count, long total, double total2, long allTimeTotal,long allTimeCount)
    {
        this.lastSample = lastSample;
        this.min = min;
        this.minInstantMs = minInstantMs;
        this.max = max;
        this.maxInstantMs = maxInstantMs;
        this.durationNs = durationNs;
        this.count = count;
        this.total = total;
        this.total2 = total2;
        this.allTimeTotal=allTimeTotal;
        this.allTimeCount=allTimeCount;
        this.createdMs = System.currentTimeMillis();
    }

    public long getAllTimeCount()
    {
        return this.allTimeCount;
    }
    public long getAllTimeTotal()
    {
        return this.allTimeTotal;
    }
    public ValueRateSample getLastSample()
    {
        return this.lastSample;
    }

    public double getAverage()
    {
        return (double)this.total/(double)this.count;
    }
    

    public double getStandardDeviation()
    {
        double b=((double)this.total/this.count)*this.total;
        double d = this.total2 - b;
        if (d <= 0.0)
        {
            return 0;
        }
        return Math.sqrt(d/this.count);
    }

    public double getRate()
    {
        if (this.durationNs==0)
        {
            return 0;
        }
        return (1.0e9*count)/this.durationNs;
    }
    private double blend(double value,double last)
    {
        double weight=(double)this.durationNs/(this.durationNs+this.lastSample.durationNs);
        return weight*value+(1.0-weight)*last;
        
    }
    public double getWeightedAverage()
    {
        if (this.lastSample==null)
        {
            return this.getAverage();
        }
        return blend(this.getAverage(),this.lastSample.getAverage());
    }
    public double getWeightedRate()
    {
        if (this.lastSample==null)
        {
            return this.getRate();
        }
        return blend(this.getRate(),this.lastSample.getRate());
    }
    public double getWeightedStandardDeviation()
    {
        if (this.lastSample==null)
        {
            return this.getStandardDeviation();
        }
        return blend(this.getStandardDeviation(),this.lastSample.getStandardDeviation());
    }

    public long getCount()
    {
        return this.count;
    }
    public long getTotal()
    {
        return this.total;
    }

    public long getMinNs()
    {
        return this.min;
    }
    public double getMinS()
    {
        return this.min/1.0e9;
    }
    public long getMaxNs()
    {
        return this.max;
    }
    public double getMaxS()
    {
        return this.max/1.0e9;
    }
    
    
    public long getCreatedMs()
    {
        return this.createdMs;
    }
    public long getMinInstantMs()
    {
        return this.minInstantMs;
    }
    public long getMaxInstantMs()
    {
        return this.maxInstantMs;
    }
    public long getDurationNs()
    {
        return this.durationNs;
    }
    public double getDurationS()
    {
        return this.durationNs/1.0e9;
    }
    

}
