package org.nova.metrics;

public class CountAverageRateMeter 
{
	private long lastCount;
	private double lastRate;
	private double lastAverage;
	private double lastStandardDeviation;
	private long mark;
	private long markCount;
	private long markSum;
	private long markSum2;
	private long total;

	public void update(long value)
	{
		synchronized(this)
		{
			this.markCount++;
			this.markSum+=value;
			this.markSum2+=value*value;
			this.total+=value;
		}
	}

	private double calculateStandardDeviation(double average,double sum,double sum2,long count)
	{
		double d=sum2-sum*average;
		if (d<0)
		{
			d=0;
		}
		else
		{
			d=d/count;
		}
		return Math.sqrt(d);
	}
	private double blend(double mark,double last,double markWeight)
	{
		return markWeight*mark+(1.0-markWeight)*last;
	}
	
	public long getTotal()
	{
		return this.total;
	}
	
	public long getCount()
	{
		synchronized (this)
		{
			return this.lastCount+this.markCount;
		}
	}
	
	public AverageAndRate getMarkCountAverage(double samplingInterval)
	{
		long now=System.currentTimeMillis();
		synchronized (this)
		{
			if (this.markCount==0)
			{
				return null;
			}
			double interval=(now-this.mark)/1000.0;
			if (interval<=samplingInterval)
			{
				return null;
			}
			
			double average=this.markSum/this.markCount;
			double standardDeviation=calculateStandardDeviation(average, this.markSum, this.markSum2, this.markCount);
			double rate=this.markCount/interval;
			return new AverageAndRate(average, standardDeviation,rate);
		}
	}
	
	public AverageAndRate getCountAverageRate(double samplingInterval)
	{
		long now=System.currentTimeMillis();
		synchronized (this)
		{
			double interval=(now-this.mark)/1000.0;
			if ((interval<=samplingInterval)&&(this.markCount>0))
			{
				if (interval<=0)
				{
					if (this.lastCount==0)
					{
						return null;
					}
					return new AverageAndRate(this.lastAverage, this.lastStandardDeviation,this.lastRate);
				}
				double markWeight=interval/samplingInterval;
				double markRate=this.markCount/interval;
				double markAverage=this.markSum/this.markCount;
				double markStandardDeviation=calculateStandardDeviation(markAverage, this.markSum, this.markSum2, this.markCount);
				return new AverageAndRate(blend(markAverage,this.lastAverage,markWeight), blend(markStandardDeviation,this.lastStandardDeviation,markWeight),blend(markRate,this.lastRate,markWeight));
			}
			
			if (this.markCount==0)
			{
				return null;
			}
			this.lastAverage=this.markSum/this.markCount;
			this.lastStandardDeviation=calculateStandardDeviation(this.lastAverage, this.markSum, this.markSum2, this.markCount);
			this.lastRate=this.markCount/interval;
			this.lastCount+=this.markCount;
			this.markCount=0;
			this.markSum=0;
			this.markSum2=0;
			this.mark=now;
			return new AverageAndRate(this.lastAverage, this.lastStandardDeviation,this.lastRate);
		}
	}
}
