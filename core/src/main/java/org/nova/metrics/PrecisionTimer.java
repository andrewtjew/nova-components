package org.nova.metrics;

public class PrecisionTimer
{
	private long start; 
	private long stop; 
	public PrecisionTimer()
	{
		
	}
	public void start()
	{
		this.start=System.nanoTime();
	}
	public long getCurrentElapsed()
	{
		return System.nanoTime()-this.start;
	}
	public long stop()
	{
		this.stop=System.nanoTime();
		return getElapsed();
	}
	public long getElapsed()
	{
		return this.stop-this.start;
	}
	public double getElapsedSeconds()
	{
		return getElapsed()/1.0e9;
	}
	
	
}
