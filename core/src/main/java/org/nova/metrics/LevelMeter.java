package org.nova.metrics;

public class LevelMeter
{
	private long level;
	private long maxLevel;
	private long maxLevelInstantMs;
	
	public LevelMeter()
	{
	}
	
	public void add(long value)
	{
		synchronized (this)
		{
			this.level+=value;
			if (this.level>=this.maxLevel)
			{
				this.maxLevel=level;
				this.maxLevelInstantMs=System.currentTimeMillis();
			}
		}
	}
	
	public void set(long value)
	{
		synchronized (this)
		{
			this.level=value;
			if (this.level>=this.maxLevel)
			{
				this.maxLevel=level;
				this.maxLevelInstantMs=System.currentTimeMillis();
			}
		}
	}
	
	public void increment()
	{
		synchronized (this)
		{
			this.level++;
			if (this.level>=this.maxLevel)
			{
				this.maxLevel=level;
				this.maxLevelInstantMs=System.currentTimeMillis();
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

	public void reset()
	{
		synchronized (this)
		{
			this.maxLevel=0;
			this.maxLevelInstantMs=System.currentTimeMillis();
			if (this.level>=this.maxLevel)
			{
				this.maxLevel=level;
			}
		}
	}
	
	public LevelSample sample()
	{
	    synchronized (this)
	    {
	        return new LevelSample(this.level,this.maxLevel,this.maxLevelInstantMs);
	    }
	}

	public long getLevel()
    {
        synchronized (this)
        {
            return this.level;
        }
    }
}
