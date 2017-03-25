package org.nova.metrics;

public class BenchmarkTimer
{
	final private double rate;
	final private PrecisionTimer timer;
	
	static public interface Code<LONG>
	{
	    void execute(Long i);
	}
	
	public BenchmarkTimer(long count,Code<Long> executable) throws Throwable
	{
		this.timer=new PrecisionTimer();
		this.timer.start();
		for (long i=0;i<count;i++)
		{
			executable.execute(i);
		}
		this.timer.stop();
		this.rate=((double)count*1.0e9)/timer.getCurrentElapsed();
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
