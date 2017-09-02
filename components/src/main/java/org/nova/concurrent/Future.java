package org.nova.concurrent;

import org.nova.tracing.Trace;
import org.nova.tracing.TraceCallable;
import org.nova.tracing.TraceManager;

public class Future<RESULT>
{
	final FutureTask<?>[] tasks;
	final private long number;
	final private Trace trace;
    private int waiting;
    private int completed;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	Future(TraceManager traceManager,Trace parent,String traceCategory,long number,TraceCallable<?>[] executables)
	{
		this.number=number;
		this.waiting=executables.length;
		
		this.tasks=new FutureTask<?>[executables.length];
		
		this.trace=new Trace(traceManager,parent,traceCategory,true);
		for (int i=0;i<executables.length;i++)
		{
			this.tasks[i]=new FutureTask(traceManager,this.trace,i+"/"+executables.length+"@"+traceCategory+"@Scheduler",executables[i]);
		}
	}
	
	void startTask()
	{
        synchronized (this)
        {
            this.waiting--;
            if (this.waiting==0)
            {
                this.trace.close();
            }
        }
	}
	
	boolean completeTask()
	{
		synchronized (this)
		{
		    this.completed++;
		    this.notifyAll();
		    return this.completed>=this.tasks.length;
		}
	}
	@SuppressWarnings("unchecked")
	public FutureTask<RESULT> getCallableTask(int index)
	{
		return (FutureTask<RESULT>)this.tasks[index];
	}

	@SuppressWarnings("unchecked")
	public RESULT get(int index) throws Throwable
	{
		return (RESULT)this.tasks[index].get();
	}

	@SuppressWarnings("unchecked")
	public RESULT getResult(int index)
	{
		return (RESULT)this.tasks[index].getResult();
	}

	public Throwable getThrowable(int index)
	{
		return this.tasks[index].getThrowable();
	}

	public RESULT get() throws Throwable 
	{
		return get(0);
	}

	public RESULT getResult() 
	{
		return getResult(0);
	}

	public Throwable getThrowable()
	{
		return getThrowable(0);
	}

	public Trace getTrace()
	{
		return trace;
	}
	public long getNumber()
	{
		return this.number;
	}
	
	public boolean waitAll(long timeout)
	{
		synchronized(this)
		{
			return Synchronization.waitForNoThrow(this, ()->{return this.completed>=this.tasks.length;},timeout);
		}
	}
	public void waitAll()
	{
		synchronized(this)
		{
			Synchronization.waitForNoThrow(this, ()->{return this.completed>=this.tasks.length;});
		}
	}
	public boolean waitAtLeast(long timeout,int count)
	{
		synchronized(this)
		{
			return Synchronization.waitForNoThrow(this, ()->{return this.completed>=count;},timeout);
		}
	}
	public void waitAtLeast(int count)
	{
		synchronized(this)
		{
			Synchronization.waitForNoThrow(this, ()->{return this.completed>=count;});
		}
	}

	public boolean waitAny(long timeout)
	{
		synchronized(this)
		{
			return Synchronization.waitForNoThrow(this, ()->{return this.completed>0;},timeout);
		}
	}
	public void waitAny()
	{
		synchronized(this)
		{
			Synchronization.waitForNoThrow(this, ()->{return this.completed>0;});
		}
	}

	public int getCompleted()
	{
		synchronized(this)
		{
			return this.completed;
		}
	}

    public int getWaiting()
    {
        synchronized(this)
        {
            return this.waiting;
        }
    }

	public int getExecuting()
	{
		synchronized(this)
		{
			return this.tasks.length-this.waiting-this.completed;
		}
	}
	
	public boolean allCompleted()
	{
		synchronized(this)
		{
			return this.completed>=this.tasks.length;
		}
	}
	public boolean anyCompleted()
	{
		synchronized(this)
		{
			return this.completed>0;
		}
	}
	public int size()
	{
		return this.tasks.length;
	}
}
