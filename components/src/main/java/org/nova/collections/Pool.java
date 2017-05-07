package org.nova.collections;

import java.util.LinkedList;
import java.util.Stack;

import org.nova.concurrent.Synchronization;
import org.nova.metrics.CountMeter;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class Pool<RESOURCE extends Resource>
{
	final private LinkedList<RESOURCE> container;
	final TraceManager traceManager;
	final private CountMeter waitingMeter;
	final private CountMeter inUseMeter;
	final private CountMeter availableMeter;
	final private long maximumRecentlyUsedCount;
	public Pool(TraceManager traceManager,long maximumRecentActivateCount)
	{
		this.waitingMeter=new CountMeter();
		this.inUseMeter=new CountMeter();
		this.availableMeter=new CountMeter();
		this.traceManager=traceManager;
		this.container=new LinkedList<>();
		this.maximumRecentlyUsedCount=maximumRecentActivateCount;
	}
	
	@SuppressWarnings("resource")
	public RESOURCE waitForAvailable(Trace parent,String traceCategory,long timeout) throws Throwable
	{
		Trace trace=new Trace(this.traceManager,parent, traceCategory,true);
		this.waitingMeter.increment();
		try
		{
			synchronized (this)
			{
				try
				{
					if (Synchronization.waitForNoThrow(this,()->{return container.size()>0;},timeout))
					{
						RESOURCE resource=container.pop();
						this.inUseMeter.increment();
						resource.activate(trace);
						trace.endWait();
						return resource;
					}
				}
				catch (Throwable t)
				{
					trace.close(t);
					throw t;
				}
			}
		}
		finally
		{
			this.waitingMeter.decrement();
		}
		trace.close();
		return null;
	}
	@SuppressWarnings("resource")
	public RESOURCE waitForAvailable(Trace parent,String traceCategory) throws Throwable
	{
		Trace trace=new Trace(this.traceManager,parent, traceCategory,true);
		this.waitingMeter.increment();
		try
		{
			synchronized (this)
			{
				try
				{
					Synchronization.waitForNoThrow(this,()->{return container.size()>0;});
					RESOURCE resource=container.pop();
					this.inUseMeter.increment();
					resource.activate(trace);
					trace.endWait();
					return resource;
				}
				catch (Throwable t)
				{
					trace.close(t);
					throw t;
				}
			}
		}
		finally
		{
			this.waitingMeter.decrement();
		}
	}
	/*
	@SuppressWarnings("resource")
	public RESOURCE allocate(Trace parent,String traceCategory) throws Throwable
	{
		Trace trace=new Trace(this.traceManager,parent, traceCategory,true);
		synchronized (this)
		{
			try
			{
				Condition.waitForNoThrowPredicate(this,()->{return stack.size()>0;});
				RESOURCE resource=stack.pop();
				resource.activate(trace);
				trace.endWait();
				return resource;
			}
			catch (Throwable t)
			{
				trace.close(t);
				throw t;
			}
		}
	}
	*/

	public void add(RESOURCE resource)
	{
		synchronized (this)
		{
			this.availableMeter.increment();
			container.add(resource);
		}
	}
	
	@SuppressWarnings("unchecked")
    void release(Resource resource)
	{
		synchronized (this)
		{
		    if (resource.canAddLast(this.maximumRecentlyUsedCount))
		    {
		        container.addLast((RESOURCE)resource);
		    }
		    else
		    {
		        container.addFirst((RESOURCE)resource);
		    }
			if (container.size()==1)
			{
				this.notify();
			}
		}
        this.inUseMeter.decrement();
	}
}
