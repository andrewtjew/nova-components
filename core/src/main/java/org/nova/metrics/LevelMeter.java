package org.nova.metrics;

public class LevelMeter
{
	private long level;
    private long maxLevel;
    private long maxLevelInstantMs;
    private long minLevel;
    private long minLevelInstantMs;
	final private long baseLevel;
	
    public LevelMeter()
    {
        this(0);
    }

    public LevelMeter(long baseLevel)
    {
        this.baseLevel=baseLevel;
        this.level=this.minLevel=this.maxLevel=this.baseLevel;
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
            if (this.level<=this.minLevel)
            {
                this.minLevel=level;
                this.minLevelInstantMs=System.currentTimeMillis();
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
	        if (this.level<=this.minLevel)
	        {
	            this.minLevel=level;
	            this.minLevelInstantMs=System.currentTimeMillis();
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
	        if (this.level<=this.minLevel)
	        {
	            this.minLevel=level;
	            this.minLevelInstantMs=System.currentTimeMillis();
	        }
		}
	}

	public void reset()
	{
		synchronized (this)
		{
			this.level=this.minLevel=this.maxLevel=this.baseLevel;
		}
	}
	
	public LevelSample sample()
	{
	    synchronized (this)
	    {
	        return new LevelSample(this.level,this.baseLevel,this.maxLevel,this.maxLevelInstantMs,this.minLevel,this.minLevelInstantMs);
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
