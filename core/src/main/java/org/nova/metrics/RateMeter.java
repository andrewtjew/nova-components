package org.nova.metrics;

public class RateMeter 
{
	private long lastCount;
	private double lastRate;
	private long markTime;
	private long markCount;
	
	public RateMeter()
	{
		this.markTime=System.currentTimeMillis();
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
	public double sampleRate(double samplingInterval)
	{
		long now=System.currentTimeMillis();
		double interval=(now-markTime)/1000.0;
		if (interval<=samplingInterval)
		{
			if (interval<=0)
			{
				return this.lastRate;
			}
			double markWeight=interval/samplingInterval;
			return markWeight*this.markCount/interval+(1.0-markWeight)*this.lastRate;
		}
		this.lastRate=this.markCount/interval;
		this.lastCount+=this.markCount;
		this.markTime=now;
		this.markCount=0;
		return this.lastRate;
	}

}
