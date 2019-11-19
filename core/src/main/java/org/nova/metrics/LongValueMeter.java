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

public class LongValueMeter 
{
    private LongValueSample lastSample;
    
    private long min;
    private long max;
    private long minInstantMs;
    private long maxInstantMs;

    private long markNs;
    private long samples;
    private long totalCount;
    private long total;
	private long markTotal;
	private double markTotal2;
	private long value;

	public LongValueMeter()
	{
	    this.min=Long.MAX_VALUE;
	    this.max=Long.MIN_VALUE;
	    this.lastSample=new LongValueSample(null, 0,0,0,0,0,0,0,0,0,0,0);
	    this.markNs=System.nanoTime();
	}
	
	public void update(long value)
	{
		synchronized(this)
		{
		    this.value=value;
			this.samples++;
			this.totalCount++;
			this.markTotal+=value;
			this.markTotal2+=value*value;
			this.total+=value;
			
			if (value>=this.max)
			{
			    this.max=value;
			    this.maxInstantMs=System.currentTimeMillis();
			}
			if (value<=this.min)
			{
			    this.min=value;
			    this.minInstantMs=System.currentTimeMillis();
			}
		}
	}

    public long getTotalCount()
    {
        synchronized(this)
        {
            return this.totalCount;
        }
    }

    public long getTotal()
    {
        synchronized(this)
        {
            return this.total;
        }
    }

	public LongValueSample sample()
	{
	    synchronized(this)
	    {
	        return sample(1);
	    }
	}
	
//	public long getValue()
//	{
//        synchronized(this)
//        {
//            return this.value;
//        }
//	}
	
	public void resetExtremas()
	{
        synchronized (this)
        {
        	this.min=Long.MAX_VALUE;
            this.max=Long.MIN_VALUE;
        }
	}	

	public LongValueSample sample(long minimalResetCount)
    {
        long nowNs=System.nanoTime();
        synchronized (this)
        {
            long durationNs=nowNs-this.markNs;
            if (durationNs<=0)
            {
                return this.lastSample;
            }
            if (this.lastSample!=null)
            {
                this.lastSample.lastSample=null;
            }
            LongValueSample result=new LongValueSample(this.lastSample, this.value, this.min, this.minInstantMs, this.max, this.maxInstantMs, durationNs, this.samples, this.markTotal, this.markTotal2,this.totalCount,this.total);
            if (this.samples>minimalResetCount)
            {
                this.lastSample=result;
                this.markNs=nowNs;
                this.samples=0;
                this.markTotal=0;
                this.markTotal2=0;
            }
            return result;
        }
    }
	
}
