package org.nova.metrics;

public class CountMeter 
{
	private long count;
	
	public CountMeter()
	{
	}

	CountMeter(long count)
	{
		this.count=count;
	}
	
	public void add(int value)
	{
		synchronized (this)
		{
			this.count+=value;
		}
	}
	
	public void set(int value)
	{
		synchronized (this)
		{
			this.count=value;
		}
	}
	
	public void increment()
	{
		synchronized (this)
		{
			this.count++;
		}
	}

	public void decrement()
	{
		synchronized (this)
		{
			this.count--;
		}
	}

	public long getCount()
	{
		synchronized (this)
		{
			return count;
		}
	}
	
	public CountMeter getSnapshot()
	{
		synchronized (this)
		{
			return new CountMeter(this.count);
		}
		
	}
}
