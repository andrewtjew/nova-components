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

public class LongValueSample
{
    LongValueSample lastSample;
    final private long min;
    final private long minInstantMs;
    final private long max;
    final private long maxInstantMs;

    final private long durationNs;
    final private long samples;
    final private long total;
    final private double total2;
    final private long createdMs;
    final private long meterTotalCount;
    final private long meterTotal;
    final private long value;

    public LongValueSample(LongValueSample lastSample, long value,long min, long minInstantMs, long max, long maxInstantMs, long durationNs, long samples, long total, double total2, long meterTotalCount,long meterTotal)
    {
        this.lastSample = lastSample;
        this.value=value;
        this.min = min;
        this.minInstantMs = minInstantMs;
        this.max = max;
        this.maxInstantMs = maxInstantMs;
        this.durationNs = durationNs;
        this.samples = samples;
        this.total = total;
        this.total2 = total2;
        this.meterTotalCount=meterTotalCount;
        this.meterTotal=meterTotal;
        this.createdMs = System.currentTimeMillis();
    }

    public long getMeterTotalCount()
    {
        return this.meterTotalCount;
    }
    public long getMeterTotal()
    {
        return this.meterTotal;
    }
    public double getMeterAverage(double invalidValue)
    {
        if (this.meterTotalCount<1)
        {
            return invalidValue;
        }
        return (double)this.meterTotal/(double)this.meterTotalCount;
        
    }
    public LongValueSample getLastSample()
    {
        return this.lastSample;
    }

    public double getAverage()
    {
        return (double)this.total/(double)this.samples;
    }
    
    public double getAverage(double invalidValue)
    {
        if (this.samples<1)
        {
            return invalidValue;
        }
        return (double)this.total/(double)this.samples;
    }

    public double getStandardDeviation()
    {
        double b=((double)this.total/this.samples)*this.total;
        double d = this.total2 - b;
        if (d <= 0.0)
        {
            return 0;
        }
        return Math.sqrt(d/this.samples);
    }
    public long getValue()
    {
        return this.value;
    }
    
    public Long getLastValue()
    {
        if (this.lastSample==null)
        {
            return null;
        }
        return this.lastSample.value;
    }
    
    public double getStandardDeviation(double invalidValue)
    {
        if (this.samples<2)
        {
            return invalidValue;
        }
        double b=((double)this.total/this.samples)*this.total;
        double d = this.total2 - b;
        if (d <= 0.0)
        {
            return 0;
        }
        return Math.sqrt(d/this.samples);
    }


    public double getRate()
    {
        return (1.0e9*this.samples)/this.durationNs;
    }
    public double getRate(double invalidValue)
    {
        if (this.durationNs==0)
        {
            return invalidValue;
        }
        return (1.0e9*this.samples)/this.durationNs;
    }
    private double blend(double value,double last)
    {
        double weight=(double)this.samples/(this.samples+this.lastSample.samples);
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
        if ((this.samples+this.lastSample.samples)<1)
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
        if ((this.samples+this.lastSample.samples)<1)
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
        if ((this.samples+this.lastSample.samples)<1)
        {
            return invalidValue;
        }
        return blend(this.getStandardDeviation(invalidValue),this.lastSample.getStandardDeviation(invalidValue));
    }

    public long getSamples()
    {
        return this.samples;
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
    public long getSampleDurationNs()
    {
        return this.durationNs;
    }
    public double getSampleDurationS()
    {
        return this.durationNs/1.0e9;
    }
    

}
