package org.nova.metrics;

public class ValueRateMeter 
{
    private ValueRateSample lastSample;
    
    private long min;
    private long max;
    private long minInstantMs;
    private long maxInstantMs;
	private long markNs;
	private long count;
	private long total;
	private double total2;

	private long allTimeTotal;
    private long allTimeCount;
    private double minimalResetDurationMs;

	public ValueRateMeter()
	{
	    this.min=Long.MAX_VALUE;
	    this.max=Long.MIN_VALUE;
	    this.lastSample=new ValueRateSample(null, 0,0,0,0,0,0,0,0,this.allTimeTotal,this.allTimeCount);
	    this.markNs=System.nanoTime();
	}
	
	public void update(long value)
	{
		synchronized(this)
		{
            this.allTimeCount++;
            this.allTimeTotal+=value;
			this.count++;
			this.total+=value;
			this.total2+=value*value;
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

	public long getAllTimeTotal()
	{
	    synchronized(this)
	    {
	        return this.allTimeTotal;
	    }
	}

	public ValueRateSample sampleAndReset()
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
            ValueRateSample sample=new ValueRateSample(this.lastSample, this.min, this.minInstantMs, this.max, this.maxInstantMs, durationNs, this.count, this.total, this.total2,this.allTimeTotal,this.allTimeCount);
            this.lastSample=sample;
            
            this.min=Long.MAX_VALUE;
            this.max=Long.MIN_VALUE;
            this.markNs=nowNs;
            this.count=0;
            this.total=0;
            this.total2=0;
            return sample;
        }
    }
	public ValueRateSample sample()
	{
	    synchronized(this)
	    {
	        return sample(this.minimalResetDurationMs);
	    }
	}
	
    public ValueRateSample sample(double minimalResetDurationS)
    {
        long nowNs=System.nanoTime();
        synchronized (this)
        {
            this.minimalResetDurationMs=minimalResetDurationS;
            long durationNs=nowNs-this.markNs;
            if (durationNs<=0)
            {
                return this.lastSample;
            }
            if (this.lastSample!=null)
            {
                this.lastSample.lastSample=null;
            }
            ValueRateSample result=new ValueRateSample(this.lastSample, this.min, this.minInstantMs, this.max, this.maxInstantMs, durationNs, this.count, this.total, this.total2,this.allTimeTotal,this.allTimeCount);
            if (durationNs>(long)(minimalResetDurationS*1.0e9))
            {
                this.lastSample=result;
                this.min=Long.MAX_VALUE;
                this.max=Long.MIN_VALUE;
                this.markNs=nowNs;
                this.count=0;
                this.total=0;
                this.total2=0;
            }
            return result;
        }
    }
	
}
