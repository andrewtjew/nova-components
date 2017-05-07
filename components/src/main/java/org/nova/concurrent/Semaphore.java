package org.nova.concurrent;

import org.nova.metrics.CountAverageRateMeter;

public class Semaphore
{
	final private Object lock;
	final private int count;
	private CountAverageRateMeter meter=new CountAverageRateMeter();
	
	private int running;
	private int waiters;
	
	public Semaphore(int count)
	{
		this.lock=new Object();
		this.count=count;
	}
	
	public long enter()
	{
		long start=System.currentTimeMillis();
		synchronized(this.lock)
		{
			if (this.running>=this.count)
			{
				this.waiters++;
				Synchronization.waitForNoThrow(this.lock, ()->{return this.running<this.count;});
				this.waiters--;
				this.meter.update(System.nanoTime()-start);
			}
			this.running++;
		}
		return start;
	}
	
	public void leave(long start)
	{
		synchronized(this.lock)
		{
			this.running--;
			if (this.waiters>0)
			{
				this.lock.notify();
			}
			this.meter.update(System.nanoTime()-start);
		}
	}
}
