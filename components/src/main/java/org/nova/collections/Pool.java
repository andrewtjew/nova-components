package org.nova.collections;

import java.util.LinkedList;
import java.util.Stack;

import org.nova.concurrent.Synchronization;
import org.nova.metrics.CountMeter;
import org.nova.metrics.LevelMeter;
import org.nova.metrics.LongSizeMeter;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class Pool<RESOURCE extends Resource>
{
	final private LinkedList<RESOURCE> container;
	final TraceManager traceManager;
	final private LevelMeter waitingMeter;
	final private LevelMeter inUseMeter;
    final private CountMeter availableMeter;
    final private CountMeter usedMeter;
    final private LongSizeMeter waitNsMeter;
	final private long maximumRecentlyUsedCount;
	public Pool(TraceManager traceManager,long maximumRecentActivateCount)
	{
		this.waitingMeter=new LevelMeter();
		this.inUseMeter=new LevelMeter();
		this.availableMeter=new CountMeter();
		this.waitNsMeter=new LongSizeMeter();
		this.usedMeter=new CountMeter();
		this.traceManager=traceManager;
		this.container=new LinkedList<>();
		this.maximumRecentlyUsedCount=maximumRecentActivateCount;
	}
	
	public LevelMeter getWaitingMeter()
    {
        return waitingMeter;
    }

    public LevelMeter getInUseMeter()
    {
        return inUseMeter;
    }

    public CountMeter getAvailableMeter()
    {
        return availableMeter;
    }
    
    public CountMeter getUsedMeter()
    {
        return this.usedMeter;
    }

    public LongSizeMeter getWaitNsMeter()
    {
        return this.waitNsMeter;
    }

    @SuppressWarnings("resource")
	public RESOURCE waitForAvailable(Trace parent,String traceCategory,long timeout) throws Throwable
	{
		
		try (Trace trace=new Trace(this.traceManager,parent, traceCategory))
		{
			synchronized (this)
			{
				try
				{
                    if (container.size()==0)
                    {
                        this.waitingMeter.increment();
                        try
                        {
                            trace.beginWait();
                            if (Synchronization.waitForNoThrow(this,()->{return container.size()>0;},timeout)==false)
                            {
                                return null;
                            }
                        }
                        finally
                        {
                            this.waitNsMeter.update(trace.getWaitNs());
                            this.waitingMeter.decrement();
                        }
                    }				    
                    RESOURCE resource=container.pop();
                    this.inUseMeter.increment();
                    resource.activate(parent);
                    return resource;
				}
				catch (Throwable t)
				{
					trace.close(t);
					throw t;
				}
			}
		}
	}
	@SuppressWarnings("resource")
	public RESOURCE waitForAvailable(Trace parent,String traceCategory) throws Throwable
	{
	    return waitForAvailable(parent, traceCategory,Long.MAX_VALUE);
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
        this.usedMeter.increment();
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
