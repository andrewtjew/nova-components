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
