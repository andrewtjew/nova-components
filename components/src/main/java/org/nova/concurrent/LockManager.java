package org.nova.concurrent;

import java.util.HashMap;

import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class LockManager<KEY>
{
	final private HashMap<KEY,Slot> slots;
	private String categoryPrefix;
	private final TraceManager traceManager;
	
	public LockManager(TraceManager traceManager,String categoryPrefix)
	{
		this.slots=new HashMap<>();
		this.traceManager=new TraceManager();
		this.categoryPrefix=this.getClass().getSimpleName()+":"+categoryPrefix+":";
	}
	
	public Lock<KEY> waitForLock(KEY key) throws Exception
	{
	    return waitForLock(key,Long.MAX_VALUE);
	}

	public Lock<KEY> waitForLock(KEY key,long timeoutMs)
    {
        Slot slot;
        Trace trace=new Trace(this.traceManager, this.categoryPrefix+key,true);
        synchronized (this)
        {
            slot=this.slots.get(key);
            if (slot==null)
            {
                slot=new Slot();
                this.slots.put(key, slot);
            }
        }
        final Slot finalSlot=slot;
        synchronized (finalSlot)
        {
            if (finalSlot.locked)
            {
                slot.waiting++;
                try
                {
                    if (Synchronization.waitForNoThrow(slot, ()->{return finalSlot.locked==false;},timeoutMs)==false)
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
            finalSlot.locked=true;
        }
        trace.endWait();
        return new Lock<KEY>(key,this,slot,trace);
    }

	void release(KEY key,Slot slot)
	{
		synchronized (slot)
		{
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
