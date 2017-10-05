package org.nova.metrics;

public class PrecisionTimer
{
	private long startNs; 
	private long stopNs;
	private long elapsedNs;
	public PrecisionTimer()
	{
		
	}
	public void start()
	{
		this.startNs=System.nanoTime();
	}
	public long getCurrentElapsed()
	{
		return System.nanoTime()-this.startNs;
	}
	public void stop()
	{
	    long now=System.nanoTime();
	    this.elapsedNs+=now-this.startNs;
	    this.startNs=now;
	}
	public void reset()
	{
	    this.elapsedNs=0;
	}
	public long getElapsedNs()
	{
		return this.elapsedNs;
	}
    public long getElapsedMs()
    {
        return getElapsedNs()/1000000;
    }
	
    public double getElapsedS()
    {
        return getElapsedNs()/1.0e9;
    }
	
	
}
