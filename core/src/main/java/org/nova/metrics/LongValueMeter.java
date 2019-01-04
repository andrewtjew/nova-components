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
