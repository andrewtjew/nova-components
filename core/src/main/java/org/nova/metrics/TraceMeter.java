/*******************************************************************************
 * Copyright (C) 2016-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.metrics;

import org.nova.tracing.Trace;

public class TraceMeter
{
    long createdMs;
    long totalDurationNs;
    long totalWaitNs;
    double totalDuration2Ns;
    double totalWait2Ns;
    Trace maxDurationTrace;
    Trace maxWaitTrace;
    Trace minDurationTrace;
    Trace minWaitTrace;
    int count;
    Trace firstExceptionTrace;
    Trace lastExceptionTrace;
    Trace lastTrace;
    int exceptionCount;

    public TraceMeter()
    {
        this.createdMs=System.currentTimeMillis();
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
            if (this.maxDurationTrace==null)
            {
                this.maxDurationTrace=trace;
            }
            else if (durationNs>=this.maxDurationTrace.getDurationNs())
            {
                this.maxDurationTrace=trace;
            }
            if (this.minDurationTrace==null)
            {
                this.minDurationTrace=trace;
            }
            else if (durationNs<=this.minDurationTrace.getDurationNs())
            {
                this.minDurationTrace=trace;
            }
            if (this.maxWaitTrace==null)
            {
                this.maxWaitTrace=trace;
            }
            else if (waitNs>=this.maxWaitTrace.getWaitNs())
            {
                this.maxWaitTrace=trace;
            }
            if (this.minWaitTrace==null)
            {
                this.minWaitTrace=trace;
            }
            else if (waitNs<=this.minWaitTrace.getWaitNs())
            {
                this.minWaitTrace=trace;
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
            else if (sample.getLastExceptionTrace()!=null)
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
            
            if (this.maxDurationTrace==null)
            {
                this.maxDurationTrace=sample.getMaxDurationTrace();
            }
            else if (sample.getMaxDurationTrace().getDurationNs()>=this.maxDurationTrace.getDurationNs())
            {
                this.maxDurationTrace=sample.getMaxDurationTrace();
            }
            if (this.minDurationTrace==null)
            {
                this.minDurationTrace=sample.getMinDurationTrace();
            }
            else if (sample.getMinDurationTrace().getDurationNs()<=this.minDurationTrace.getDurationNs())
            {
                this.minDurationTrace=sample.getMinDurationTrace();
            }
            if (this.maxWaitTrace==null)
            {
                this.maxWaitTrace=sample.getMaxWaitTrace();
            }
            else if (sample.getMaxWaitTrace().getDurationNs()>=this.maxWaitTrace.getDurationNs())
            {
                this.maxWaitTrace=sample.getMaxWaitTrace();
            }
            if (this.minWaitTrace==null)
            {
                this.minWaitTrace=sample.getMinWaitTrace();
            }
            else if (sample.getMinWaitTrace().getDurationNs()<=this.minWaitTrace.getDurationNs())
            {
                this.minWaitTrace=sample.getMinWaitTrace();
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
