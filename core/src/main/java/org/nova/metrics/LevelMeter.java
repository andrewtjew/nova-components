package org.nova.metrics;

public class LevelMeter
{
	private long level;
	private long highestLevel;
	private long highestLevelTimeStamp;
	
	public LevelMeter()
	{
		this.highestLevelTimeStamp=System.currentTimeMillis();
	}
	
	public void add(long value)
	{
		synchronized (this)
		{
			this.level+=value;
			if (this.level>=this.highestLevel)
			{
				this.highestLevel=level;
				this.highestLevelTimeStamp=System.currentTimeMillis();
			}
		}
	}
	
	public void set(long value)
	{
		synchronized (this)
		{
			this.level=value;
			if (this.level>=this.highestLevel)
			{
				this.highestLevel=level;
				this.highestLevelTimeStamp=System.currentTimeMillis();
			}
		}
	}
	
	public void increment()
	{
		synchronized (this)
		{
			this.level++;
			if (this.level>=this.highestLevel)
			{
				this.highestLevel=level;
				this.highestLevelTimeStamp=System.currentTimeMillis();
			}
		}
	}

	public void decrement()
	{
		synchronized (this)
		{
			this.level--;
		}
	}

	public long getMaximumLevel()
	{
		synchronized (this)
		{
			return highestLevel;
		}
	}

	public long getHighestLevelTimeStamp()
	{
		synchronized (this)
		{
			return highestLevelTimeStamp;
		}
	}

	public long getLevel()
	{
		synchronized (this)
		{
			return level;
		}
	}
	
	public void reset()
	{
		synchronized (this)
		{
			this.highestLevel=0;
			this.highestLevelTimeStamp=System.currentTimeMillis();
			if (this.level>=this.highestLevel)
			{
				this.highestLevel=level;
			}
		}
	}
}
