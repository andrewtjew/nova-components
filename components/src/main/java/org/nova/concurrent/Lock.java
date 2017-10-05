package org.nova.concurrent;

import org.nova.tracing.Trace;

public class Lock<KEY> implements AutoCloseable
{
	final Slot slot;
	LockManager<KEY> lockManager;
	final Trace trace;
	final KEY key;
	
	Lock(KEY key,LockManager<KEY> lockManager,Slot lockObject,Trace trace)
	{
	    this.key=key;
		this.trace=trace;
		this.lockManager=lockManager;
		this.slot=lockObject;
	}
	
	@Override
	public void close()
	{
		synchronized (this)
		{
			if (this.lockManager==null)
			{
				return;
			}
			this.lockManager.release(this.key,this.slot);
			this.trace.close();
			this.lockManager=null;
		}
	}

}
