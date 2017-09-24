package org.nova.tracing;

public class TraceNodeSample
{
    final private long totalDurationNs;
    final private long totalWaitNs;
    final private long maxDurationNs;
    final private long maxWaitNs;
    final private long maxDurationInstantMs;
    final private long maxWaitInstantMs;
    final private long minDurationNs;
    final private long minWaitNs;
    final private long minDurationInstantMs;
    final private long minWaitInstantMs;
    final private long count;
    final private long exceptionCount;
    final private Trace lastExceptionTrace;

    TraceNodeSample(long totalDurationNs, long totalWaitNs, long maxDurationNs, long maxWaitNs, long maxDurationInstantMs, long maxWaitInstantMs, long minDurationNs, long minWaitNs,
            long minDurationInstantMs, long minWaitInstantMs, long count,long exceptions,Trace lastExceptionTrace)
    {
        this.totalDurationNs = totalDurationNs;
        this.totalWaitNs = totalWaitNs;
        this.maxDurationNs = maxDurationNs;
        this.maxWaitNs = maxWaitNs;
        this.maxDurationInstantMs = maxDurationInstantMs;
        this.maxWaitInstantMs = maxWaitInstantMs;
        this.minDurationNs = minDurationNs;
        this.minWaitNs = minWaitNs;
        this.minDurationInstantMs = minDurationInstantMs;
        this.minWaitInstantMs = minWaitInstantMs;
        this.count = count;
        this.exceptionCount=exceptions;
        this.lastExceptionTrace=lastExceptionTrace;
    }

    public double getMaxDurationS()
    {
        return getMaxDurationNs() / 1.0e9;
    }

    public long getMaxDurationNs()
    {
        return maxDurationNs;
    }

    public long getMaxInstantMs()
    {
        return this.maxDurationInstantMs;
    }

    public double getMinDurationS()
    {
        return getMinDurationNs() / 1.0e9;
    }

    public long getMinDurationNs()
    {
        return minDurationNs;
    }

    public long getMinInstantMs()
    {
        return this.minDurationInstantMs;
    }

    public double getMaxWaitS()
    {
        return getMaxWaitNs() / 1.0e9;
    }

    public long getMaxWaitNs()
    {
        return maxWaitNs;
    }
    public long getMaxWaitInstantMs()
    {
        return this.maxWaitInstantMs;
    }

    public double getMinWaitS()
    {
        return getMinWaitNs() / 1.0e9;
    }


    public long getMinWaitNs()
    {
        return minWaitNs;
    }

    public long getMinWaitInstantMs()
    {
        return this.minWaitInstantMs;
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
    public long getExceptionCount()
    {
        return this.exceptionCount;
    }
    public Trace getLastExceptionTrace()
    {
        return this.lastExceptionTrace;
    }
}
