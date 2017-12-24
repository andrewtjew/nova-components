package org.nova.metrics;

import org.nova.tracing.Trace;

public class TraceMeter
{
    long createdMs;
    long totalDurationNs;
    long totalWaitNs;
    double totalDuration2Ns;
    double totalWait2Ns;
    long maxDurationNs;
    long maxWaitNs;
    long maxDurationInstantMs;
    long maxWaitInstantMs;
    long minDurationNs;
    long minWaitNs;
    long minDurationInstantMs;
    long minWaitInstantMs;
    int count;
    Trace firstExceptionTrace;
    Trace lastExceptionTrace;
    Trace lastTrace;
    int exceptionCount;

    public TraceMeter()
    {
        this.createdMs=System.currentTimeMillis();
        this.maxDurationNs=Long.MIN_VALUE;
        this.maxWaitNs=Long.MIN_VALUE;
        this.minDurationNs=Long.MAX_VALUE;
        this.minWaitNs=Long.MAX_VALUE;
    }
    public void update(Trace trace)
    {
        long durationNs=trace.getDurationNs();
        long waitNs=trace.getWaitNs();
        synchronized(this)
        {
            if (trace.getCreatedMs()<this.createdMs)
            {
                this.createdMs=trace.getCreatedMs();
            }
            if (trace.getThrowable()!=null)
            {
                this.exceptionCount++;
                if (this.firstExceptionTrace==null)
                {
                    this.firstExceptionTrace=trace;
                }
                this.lastExceptionTrace=trace;
            }
            if (durationNs>=this.maxDurationNs)
            {
                this.maxDurationNs=durationNs;
                this.maxDurationInstantMs=System.currentTimeMillis();
            }
            if (durationNs<=this.minDurationNs)
            {
                this.minDurationNs=durationNs;
                this.minDurationInstantMs=System.currentTimeMillis();
            }
            if (waitNs>=this.maxWaitNs)
            {
                this.maxWaitNs=waitNs;
                this.maxWaitInstantMs=System.currentTimeMillis();
            }
            if (durationNs<=this.minWaitNs)
            {
                this.minWaitNs=durationNs;
                this.minWaitInstantMs=System.currentTimeMillis();
            }
            this.lastTrace=trace;
            this.totalDurationNs+=durationNs;
            this.totalDuration2Ns+=durationNs*durationNs;
            this.totalWaitNs+=waitNs;
            this.totalWait2Ns+=waitNs*waitNs;
            this.count++;
        }
    }
    public void update(TraceSample sample)
    {
        if (sample.getCount()==0)
        {
            return;
        }
        synchronized(this)
        {
            this.exceptionCount+=sample.getExceptionCount();
            if (this.lastExceptionTrace==null)
            {
                if (sample.getLastExceptionTrace()!=null)
                {
                    this.firstExceptionTrace=sample.getFirstExceptionTrace();
                    this.lastExceptionTrace=sample.getLastExceptionTrace();
                }
            }
            else if (this.sample().getLastExceptionTrace()!=null)
            {
                //Both this and sample have exception traces
                if (sample.getFirstExceptionTrace().getCreatedMs()<this.firstExceptionTrace.getCreatedMs())
                {
                    this.firstExceptionTrace=sample.getFirstExceptionTrace();
                }
                if (sample.getLastExceptionTrace().getCreatedMs()>this.lastExceptionTrace.getCreatedMs())
                {
                    this.lastExceptionTrace=sample.getLastExceptionTrace();
                }
            }
            
            if (sample.getMaxDurationNs()>=this.maxDurationNs)
            {
                this.maxDurationNs=sample.getMaxDurationNs();
                this.maxDurationInstantMs=sample.getMaxDurationInstantMs();
            }
            if (sample.getMinDurationNs()<=this.minDurationNs)
            {
                this.minDurationNs=sample.getMinDurationNs();
                this.minDurationInstantMs=sample.getMinDurationInstantMs();
            }
            if (sample.getMaxWaitNs()>=this.maxWaitNs)
            {
                this.maxWaitNs=sample.getMaxWaitNs();
                this.maxWaitInstantMs=sample.getMaxWaitInstantMs();
            }
            if (sample.getMinWaitNs()<=this.minWaitNs)
            {
                this.minWaitNs=sample.getMinWaitNs();
                this.minWaitInstantMs=sample.getMinWaitInstantMs();
            }
            this.totalDurationNs+=sample.getTotalDurationNs();
            this.totalDuration2Ns+=sample.totalDuration2Ns;
            this.totalWaitNs+=sample.getTotalWaitNs();
            this.totalWait2Ns+=sample.totalWait2Ns;
            this.count+=sample.getCount();
        }        
    }
    public TraceSample sample()
    {
        synchronized(this)
        {
            long span=System.currentTimeMillis()-this.createdMs;
            if (span<=0)
            {
                span=1;
            }
            double rate=1000.0*this.count/span;
            double exceptionRate=1000.0*this.exceptionCount/span;
            return new TraceSample(this,rate,exceptionRate);
        }
    }

}
