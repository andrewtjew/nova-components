package org.nova.concurrent;

import org.nova.tracing.Trace;
import org.nova.tracing.TraceCallable;
import org.nova.tracing.TraceManager;

public class Future<RESULT>
{
	final FutureTask<?>[] tasks;
	final private long number;
	final private Trace trace;
	private int executing;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	Future(TraceManager traceManager,Trace parent,String traceCategory,long number,TraceCallable<?>[] executables)
	{
		this.number=number;
		this.executing=executables.length;
		
		this.tasks=new FutureTask<?>[executables.length];
		
		this.trace=new Trace(traceManager,parent,traceCategory);
		for (int i=0;i<executables.length;i++)
		{
			this.tasks[i]=new FutureTask(traceManager,this.trace,traceCategory+"@Task."+i,executables[i]);
		}
	}
	
	boolean completeTask()
	{
		synchronized (this)
		{
			if (this.executing>0)
			{
				try
				{
					this.executing--;
					if (this.executing==0)
					{
						this.trace.close();
						return true;
					}
				}
				finally
				{
					this.notifyAll();
				}
			}
			return false;
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
			return Condition.waitForNoThrow(this, ()->{return this.executing==0;},timeout);
		}
	}
	public void waitAll()
	{
		synchronized(this)
		{
			Condition.waitForNoThrow(this, ()->{return this.executing==0;});
		}
	}
	public boolean waitAtLeast(long timeout,int count)
	{
		synchronized(this)
		{
			return Condition.waitForNoThrow(this, ()->{return this.tasks.length-this.executing>=count;},timeout);
		}
	}
	public void waitAtLeast(int count)
	{
		synchronized(this)
		{
			Condition.waitForNoThrow(this, ()->{return this.tasks.length-this.executing>=count;});
		}
	}

	public boolean waitAny(long timeout)
	{
		synchronized(this)
		{
			return Condition.waitForNoThrow(this, ()->{return this.executing<this.tasks.length;},timeout);
		}
	}
	public void waitAny()
	{
		synchronized(this)
		{
			Condition.waitForNoThrow(this, ()->{return this.executing<this.tasks.length;});
		}
	}

	public int getCompleted()
	{
		synchronized(this)
		{
			return this.tasks.length-this.executing;
		}
	}

	public int getExecuting()
	{
		synchronized(this)
		{
			return this.executing;
		}
	}
	
	public boolean allCompleted()
	{
		synchronized(this)
		{
			return this.executing==0;
		}
	}
	public boolean anyCompleted()
	{
		synchronized(this)
		{
			return this.executing<this.tasks.length;
		}
	}
	public int size()
	{
		return this.tasks.length;
	}
}
