package org.nova.concurrent;

import java.util.HashMap;

import org.nova.core.NoThrowPredicate;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class LockManager<KEY>
{
	final private HashMap<KEY,Slot> slots;
	private String category;
	private final TraceManager traceManager;
	
	public LockManager(TraceManager traceManager,String category)
	{
		this.slots=new HashMap<>();
		this.traceManager=traceManager;
		this.category=category+"@"+this.getClass().getSimpleName();
	}
	
	public Lock<KEY> waitForLock(Trace parent,KEY key) throws Exception
	{
	    return waitForLock(parent,key,Long.MAX_VALUE);
	}

	public Lock<KEY> waitForLock(Trace parent,KEY key,long timeoutMs)
    {
        Slot slot;
        Trace trace=new Trace(parent, this.category,true);
        trace.setDetails(key.toString());
        synchronized (this)
        {
            slot=this.slots.get(key);
            if (slot==null)
            {
                slot=new Slot();
                this.slots.put(key, slot);
            }
        }
        final Slot slot_=slot;
        synchronized (slot_)
        {
            if (slot_.locked)
            {
                slot.waiting++;
                try
                {
                    if (Synchronization.waitForNoThrow(slot, ()->{return slot_.locked==false;},timeoutMs)==false)
                    {
                        trace.close();
                        return null;
                    }
                }
                finally
                {
                    slot.waiting--;
                }
            }
            slot_.locked=true;
        }

        
        trace.endWait();
        return new Lock<KEY>(key,this,slot,trace);
    }

	void release(KEY key,Slot slot)
	{
		synchronized (slot)
		{
		    slot.locked=false;
            if (slot.waiting==0)
            {
                synchronized (this)
                {
                    this.slots.remove(key);
                    
                    return;
                }
            }
			slot.notify();
		}
	}
}
