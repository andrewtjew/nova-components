package org.nova.collections;


import org.nova.tracing.Trace;

public abstract class Resource implements AutoCloseable
{
	final private Pool<?> pool;
	private Trace parent;
	private long recentlyUsedCount;
	
	
	public Resource(Pool<?> pool)
	{
		this.pool=pool;
		this.recentlyUsedCount=0;
	}
	
	void activate(Trace parent) throws Throwable
	{
		synchronized(this)
		{
			this.parent=parent;
			activate();
		}
	}

	abstract protected void activate() throws Throwable;

	abstract protected void park() throws Exception;
	
	boolean canAddLast(long recentActivateMaximumCount)
	{
	    if (++this.recentlyUsedCount>=recentActivateMaximumCount)
	    {
	        this.recentlyUsedCount=0;
	        return true;
	    }
	    return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception
	{
		synchronized(this)
		{
			if (this.parent!=null)
			{
                park();
                pool.release(this);
				this.parent=null;
			}
		}
	}
	
	public Trace getParent()
	{
	    return this.parent;
	}
}
