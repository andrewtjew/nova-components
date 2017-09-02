package org.nova.collections;


import org.nova.tracing.Trace;

public abstract class Resource implements AutoCloseable
{
	final private Pool<?> pool;
	private Trace trace;
	private long recentlyUsedCount;
	
	
	public Resource(Pool<?> pool)
	{
		this.pool=pool;
		this.recentlyUsedCount=0;
	}
	
	void activate(Trace trace) throws Throwable
	{
		synchronized(this)
		{
			this.trace=trace;
			try
			{
				activate();
			}
			catch (Throwable t)
			{
				trace.close(t);
				throw t;
			}
		}
	}

	abstract protected void activate() throws Throwable;

	abstract protected void park() throws Throwable;
	
	boolean canAddLast(long recentActivateMaximumCount)
	{
	    if (++this.recentlyUsedCount>=recentActivateMaximumCount)
	    {
	        this.recentlyUsedCount=0;
	        return true;
	    }
	    return false;
	}

	@Override
	public void close() throws Exception
	{
		synchronized(this)
		{
			if (this.trace!=null)
			{
				try
				{
					park();
	                pool.release(this);
					this.trace.close();
				}
				catch (Throwable t)
				{
					this.trace.close(t);
				}
				this.trace=null;
			}
		}
	}
}
