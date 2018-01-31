package org.nova.metrics;

public class LongValueSample
{
    LongValueSample lastSample;
    final private long min;
    final private long minInstantMs;
    final private long max;
    final private long maxInstantMs;

    final private long durationNs;
    final private long count;
    final private long total;
    final private double total2;
    final private long createdMs;
    final private long totalCount;

    public LongValueSample(LongValueSample lastSample, long min, long minInstantMs, long max, long maxInstantMs, long durationNs, long count, long total, double total2, long totalCount)
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
        this.totalCount=totalCount;
        this.createdMs = System.currentTimeMillis();
    }

    public long getTotalCount()
    {
        return this.totalCount;
    }
    public LongValueSample getLastSample()
    {
        return this.lastSample;
    }

    public double getAverage()
    {
        return (double)this.total/(double)this.count;
    }
    
    public double getAverage(double invalidValue)
    {
        if (this.count<1)
        {
            return invalidValue;
        }
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

    public double getStandardDeviation(double invalidValue)
    {
        if (this.count<2)
        {
            return invalidValue;
        }
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
        return (1.0e9*this.count)/this.durationNs;
    }
    public double getRate(double invalidValue)
    {
        if (this.durationNs==0)
        {
            return invalidValue;
        }
        return (1.0e9*this.count)/this.durationNs;
    }
    private double blend(double value,double last)
    {
        double weight=(double)this.count/(this.count+this.lastSample.count);
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
    public double getWeightedAverage(double invalidValue)
    {
        if (this.lastSample==null)
        {
            return this.getAverage(invalidValue);
        }
        if ((this.count+this.lastSample.count)<1)
        {
            return invalidValue;
        }
        return blend(this.getAverage(invalidValue),this.lastSample.getAverage(invalidValue));
    }
    public double getWeightedRate()
    {
        if (this.lastSample==null)
        {
            return this.getRate();
        }
        return blend(this.getRate(),this.lastSample.getRate());
    }
    public double getWeightedRate(double invalidValue)
    {
        if (this.lastSample==null)
        {
            return this.getRate(invalidValue);
        }
        if ((this.count+this.lastSample.count)<1)
        {
            return invalidValue;
        }
        return blend(this.getRate(invalidValue),this.lastSample.getRate(invalidValue));
    }
    public double getWeightedStandardDeviation()
    {
        if (this.lastSample==null)
        {
            return this.getStandardDeviation();
        }
        return blend(this.getStandardDeviation(),this.lastSample.getStandardDeviation());
    }
    public double getWeightedStandardDeviation(double invalidValue)
    {
        if (this.lastSample==null)
        {
            return this.getStandardDeviation(invalidValue);
        }
        if ((this.count+this.lastSample.count)<1)
        {
            return invalidValue;
        }
        return blend(this.getStandardDeviation(invalidValue),this.lastSample.getStandardDeviation(invalidValue));
    }

    public long getCount()
    {
        return this.count;
    }
    public long getTotal()
    {
        return this.total;
    }

    public long getMin()
    {
        return this.min;
    }
    public long getMax()
    {
        return this.max;
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
