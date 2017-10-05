package org.nova.metrics;

public class RateMeter 
{
    private RateSample lastSample;
    
    private long markNs;
    private long count;
    private long allTimeCount;
    private double minimalResetDurationS;
	
	public RateMeter()
	{
		this.markNs=System.nanoTime();
	}
	
	public void add(long count)
	{
		synchronized (this)
		{
			this.count+=count;
			this.allTimeCount+=count;
		}
	}
	
	public void increment()
	{
		synchronized (this)
		{
			this.count++;
			this.allTimeCount++;
		}
	}
	
	public long getAllTimeCount()
	{
		synchronized (this)
		{
			return this.allTimeCount;
		}
	}
	
    public RateSample sampleAndReset()
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
            RateSample sample=new RateSample(this.lastSample,durationNs, this.count, this.allTimeCount);
            this.lastSample=sample;
            
            this.markNs=nowNs;
            this.count=0;
            return sample;
        }
    }
    
    public RateSample sample()
    {
        synchronized(this)
        {
            return sample(this.minimalResetDurationS);
        }
    }
    
    public RateSample sample(double minimalResetDurationS)
    {
        long nowNs=System.nanoTime();
        synchronized (this)
        {
            this.minimalResetDurationS=minimalResetDurationS;
            long durationNs=nowNs-this.markNs;
            if (durationNs<=0)
            {
                return this.lastSample;
            }
            if (this.lastSample!=null)
            {
                this.lastSample.lastSample=null;
            }
            RateSample result=new RateSample(this.lastSample, durationNs, this.count,this.allTimeCount);
            if (durationNs>(long)(minimalResetDurationS*1.0e9))
            {
                this.lastSample=result;
                this.markNs=nowNs;
                this.count=0;
            }
            return result;
        }
    }
	
}
