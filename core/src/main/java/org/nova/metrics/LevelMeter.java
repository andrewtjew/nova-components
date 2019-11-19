/*******************************************************************************
 * Copyright (C) 2016-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
