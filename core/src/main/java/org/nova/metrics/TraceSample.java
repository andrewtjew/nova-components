package org.nova.metrics;

import org.nova.tracing.Trace;

public class TraceSample
{
    final private  double rate;
    final private long totalDurationNs;
    final double totalDuration2Ns;
    final private long totalWaitNs;
    final double totalWait2Ns;
    final private Trace maxDurationTrace;
    final private Trace maxWaitTrace;
    final private Trace minDurationTrace;
    final private Trace minWaitTrace;
    final private long count;
    final private long createdMs;
    final private Trace firstExceptionTrace;
    final private Trace lastExceptionTrace;
    final private Trace lastTrace;
    
    final private long exceptionCount;
    final private  double exceptionRate;

    TraceSample(TraceMeter meter,double rate,double exceptionRate)
    {
        this.createdMs=meter.createdMs;
        this.totalDurationNs = meter.totalDurationNs;
        this.totalDuration2Ns=meter.totalDuration2Ns;
        this.totalWaitNs = meter.totalWaitNs;
        this.totalWait2Ns=meter.totalWait2Ns;
        this.maxDurationTrace=meter.maxDurationTrace;
        this.minDurationTrace=meter.minDurationTrace;
        this.maxWaitTrace=meter.maxWaitTrace;
        this.minWaitTrace=meter.minWaitTrace;
        this.count = meter.count;
        this.firstExceptionTrace=meter.firstExceptionTrace;
        this.lastExceptionTrace=meter.lastExceptionTrace;
        this.exceptionCount=meter.exceptionCount;
        this.lastTrace=meter.lastTrace;
        this.rate=rate;
        this.exceptionRate=exceptionRate;
    }

    public long getCreatedMs()
    {
        return this.createdMs;
    }
    public double getRate()
    {
       return this.rate; 
    }

    public Trace getMaxDurationTrace()
    {
        return this.maxDurationTrace;
    }

    public Trace getMinDurationTrace()
    {
        return this.minDurationTrace;
    }

    public Trace getMaxWaitTrace()
    {
        return this.maxWaitTrace;
    }

    public Trace getMinWaitTrace()
    {
        return this.minWaitTrace;
    }



    public double getTotalDurationS()
    {
        return getTotalDurationNs() / 1.0e9;
    }

    public long getTotalDurationNs()
    {
        return totalDurationNs;
    }

    public double getTotalWaitS()
    {
        return getTotalWaitNs() / 1.0e9;
    }

    public long getTotalWaitNs()
    {
        return totalWaitNs;
    }
    public long getCount()
    {
        return count;
    }
    public double getStandardDeviationDurationNs()
    {
        double b=((double)this.totalDurationNs/this.count)*this.totalDurationNs;
        double d = this.totalDuration2Ns - b;
        if (d <= 0.0)
        {
            return 0;
        }
        return Math.sqrt(d/this.count);
    }
    public double getStandardDeviationTotalDurationS()
    {
        return this.getStandardDeviationDurationNs()/1.0e9;
    }
    public double getStandardDeviationWaitNs()
    {
        double b=((double)this.totalWaitNs/this.count)*this.totalWaitNs;
        double d = this.totalWait2Ns - b;
        if (d <= 0.0)
        {
            return 0;
        }
        return Math.sqrt(d/this.count);
    }
    public double getStandardDeviationWaitDurationS()
    {
        return this.getStandardDeviationWaitNs()/1.0e9;
    }
    public double getAverageDurationNs()
    {
        return (double)this.totalDurationNs/this.count;
    }
    public double getAverageWaitNs()
    {
        return (double)this.totalWaitNs/this.count;
    }
    public double getAverageDurationS()
    {
        return getAverageDurationNs()/1.0e9;
    }
    public double getAverageWaitS()
    {
        return getAverageDurationNs()/1.0e9;
    }
    public Trace getFirstExceptionTrace()
    {
        return firstExceptionTrace;
    }
    public Trace getLastExceptionTrace()
    {
        return lastExceptionTrace;
    }
    public long getExceptionCount()
    {
        return exceptionCount;
    }
    public double getExceptionRate()
    {
        return exceptionRate;
    }
    public Trace getLastTrace()
    {
        return this.lastTrace;
    }
}
