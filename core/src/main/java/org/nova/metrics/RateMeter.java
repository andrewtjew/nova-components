package org.nova.metrics;

public class RateMeter 
{
	private long lastCount;
    private double lastRate;
	private long markInstantNs;
	private long markCount;
	
	public RateMeter()
	{
		this.markInstantNs=System.nanoTime();
	}
	
	public void add(long count)
	{
		synchronized (this)
		{
			this.markCount+=count;
		}
	}
	
	public void increment()
	{
		synchronized (this)
		{
			this.markCount++;
		}
	}
	
	public long getCount()
	{
		synchronized (this)
		{
			return this.markCount+this.lastCount;
		}
	}
	public double sampleRate(double samplingIntervalS)
	{
		long now=System.nanoTime();
        synchronized(this)
        {
    		double intervalS=(now-this.markInstantNs)/1.0e9;
    		if (intervalS<=samplingIntervalS)
    		{
    			if (intervalS<=0)
    			{
    				return this.lastRate;
    			}
    			double markWeight=intervalS/samplingIntervalS;
    			return markWeight*this.markCount/intervalS+(1.0-markWeight)*this.lastRate;
    		}
    		this.lastRate=this.markCount/intervalS;
    		this.lastCount+=this.markCount;
    		this.markInstantNs=now;
    		this.markCount=0;
            return this.lastRate;
        }
	}
    public double sampleRate()
    {
        long now=System.nanoTime();
        synchronized(this)
        {
            if (now<=this.markInstantNs)
            {
                return this.lastRate;
            }
            double intervalS=(now-this.markInstantNs)/1.0e9;
            this.lastRate=this.markCount/intervalS;
            this.lastCount+=this.markCount;
            this.markInstantNs=now;
            this.markCount=0;
            return this.lastRate;
        }
    }
	
}
