package org.nova.collections;


import org.nova.tracing.Trace;

public abstract class Resource implements AutoCloseable
{
	final private Pool<?> pool;
    final private long identifier;
	private Trace parent;
	private long recentlyUsedCount;
    private Thread activateThread;
    private StackTraceElement[] activateStackTrace;
    private boolean activated;
	
	public Resource(Pool<?> pool)
	{
		this.pool=pool;
		this.recentlyUsedCount=0;
		this.identifier=pool.getNextIdentifier();
	}
	
	void activate(Trace parent) throws Throwable
	{
		synchronized(this)
		{
		    this.activated=true;
			this.parent=parent;
			this.activateThread=Thread.currentThread();
	        if (pool.captureActiveStackTrace())
	        {
	            this.activateStackTrace=this.activateThread.getStackTrace();
	        }
			activate();
		}
	}

	abstract protected void activate() throws Throwable;

	abstract protected void park() throws Exception;
	
	public long getIdentifier()
	{
	    return this.identifier;
	}
	
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
			if (this.activated)
			{
                park();
                pool.release(this);
				this.parent=null;
				this.activateThread=null;
				this.activateStackTrace=null;
				this.activated=false;
			}
		}
	}
	
	public Trace getParent()
	{
        synchronized(this)
        {
            return this.parent;
        }
	}
	public Thread getActivateThread()
	{
	    synchronized(this)
	    {
	        return this.activateThread;
	    }
	}
	public StackTraceElement[] getActivateStackTrace()
	{
        synchronized(this)
        {
            return this.activateStackTrace;
        }
	}
}
