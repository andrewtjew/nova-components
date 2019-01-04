package org.nova.metrics;

public class BenchmarkTimer
{
	final private double rate;
	final private PrecisionTimer timer;
	
	static public interface Code
	{
	    void execute(long i) throws Throwable;
	}
	
	public BenchmarkTimer(long count,Code code) throws Throwable
	{
		this.timer=new PrecisionTimer();
		this.timer.start();
		for (long i=0;i<count;i++)
		{
			code.execute(i);
		}
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
	
}
