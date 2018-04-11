package org.nova.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.nova.concurrent.Synchronization;
import org.nova.metrics.LevelMeter;
import org.nova.metrics.LongValueMeter;
import org.nova.metrics.RateMeter;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class Pool<RESOURCE extends Resource>
{
    final private LevelMeter waitingMeter;
    final private RateMeter useMeter;
    final private LongValueMeter waitNsMeter;
    private LevelMeter availableMeter;
    

	final private LinkedList<RESOURCE> container;
	final TraceManager traceManager;
	final private long maximumRecentlyUsedCount;
	private AtomicLong identifier;
	private boolean captureActivateStackTrace;
	final HashMap<Long,RESOURCE> InUseResources=new HashMap<>();
	
	public Pool(TraceManager traceManager,long maximumRecentActivateCount)
	{
		this.waitingMeter=new LevelMeter();
		this.waitNsMeter=new LongValueMeter();
		this.useMeter=new RateMeter();
		this.availableMeter=new LevelMeter();
		this.traceManager=traceManager;
		this.container=new LinkedList<>();
		this.maximumRecentlyUsedCount=maximumRecentActivateCount;
		this.identifier=new AtomicLong();
	}
	
	public void setCaptureActivateStackTrace(boolean captureActivateStackTrace)
	{
	    synchronized(this)
	    {
	        this.captureActivateStackTrace=captureActivateStackTrace;
	    }
	}
	public boolean captureActiveStackTrace()
	{
        synchronized(this)
        {
            return this.captureActivateStackTrace;
        }
	}
	
	public LevelMeter getWaitingMeter()
    {
        return waitingMeter;
    }

    long getNextIdentifier()
    {
        return this.identifier.getAndIncrement();
    }
    
    public LevelMeter getAvailableMeter()
    {
        return this.availableMeter;
    }
    
    public RateMeter getUseMeter()
    {
        return this.useMeter;
    }

    public LongValueMeter getWaitNsMeter()
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
                    resource.activate(parent);
                    this.InUseResources.put(resource.getIdentifier(), resource);
                    this.availableMeter.decrement();
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
	public void add(RESOURCE resource)
	{
		synchronized (this)
		{
			container.add(resource);
		}
	}
	*/
	
	public void initialize(RESOURCE[] resources)
	{
        synchronized (this)
        {
            for (RESOURCE resource:resources)
            {
                container.add(resource);
            }
            this.availableMeter=new LevelMeter(resources.length);
        }
	    
	}
	
	@SuppressWarnings("unchecked")
    void release(Resource resource)
	{
        this.useMeter.increment();
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
		    if (this.waitingMeter.getLevel()>0)
			{
				this.notify();
			}
			this.InUseResources.remove(resource.getIdentifier());
            this.availableMeter.increment();
		}
	}
	
	public List<RESOURCE> getSnapshotOfInUseResources()
	{
	    synchronized(this)
	    {
	        return new ArrayList<>(this.InUseResources.values());
	    }
	}
	
}
