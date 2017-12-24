package org.nova.metrics;

public class BenchmarkTimer
{
	final private double rate;
	final private PrecisionTimer timer;
	final private long sum;
	
	static public interface Code<LONG>
	{
	    long execute(Long i) throws Throwable;
	}
	
	public BenchmarkTimer(long count,Code<Long> executable) throws Throwable
	{
		this.timer=new PrecisionTimer();
		this.timer.start();
		long sum=0;
		for (long i=0;i<count;i++)
		{
			sum+=executable.execute(i);
		}
		this.sum=sum;
		this.timer.stop();
		this.rate=((double)count*1.0e9)/timer.getElapsedNs();
	}

	public double getRate()
	{
		return rate;
	}

	public PrecisionTimer getTimer()
	{
		return timer;
	}
	public long getSum()
	{
	    return this.sum;
	}
	
}
