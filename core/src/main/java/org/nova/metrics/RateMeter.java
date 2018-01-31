package org.nova.metrics;

public class RateMeter 
{
    private RateSample lastSample;
    
    private long markNs;
    private long markCount;
    private long totalCount;
	
	public RateMeter()
	{
		this.markNs=System.nanoTime();
	}
	
	public void add(long count)
	{
		synchronized (this)
		{
			this.markCount+=count;
			this.totalCount+=count;
		}
	}
	
	public void increment()
	{
		synchronized (this)
		{
			this.markCount++;
			this.totalCount++;
		}
	}
	
	public long getTotalCount()
	{
		synchronized (this)
		{
			return this.totalCount;
		}
	}
    
    public RateSample sample()
    {
        synchronized(this)
        {
            return sample(1);
        }
    }
    
    public RateSample sample(double minimalResetDurationS)
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
            RateSample result=new RateSample(this.lastSample, durationNs, this.markCount,this.totalCount);
            if (durationNs>(long)(minimalResetDurationS*1.0e9))
            {
                this.lastSample=result;
                this.markNs=nowNs;
                this.markCount=0;
            }
            return result;
        }
    }
	
}
