package org.nova.concurrent;

import org.nova.tracing.Trace;
import org.nova.tracing.TraceCallable;
import org.nova.tracing.TraceManager;

public class FutureTask<RESULT>
{
	private TaskStatus status;
	private RESULT result;
	private Throwable throwable;
	final private Trace scheduleTrace;
	final private TraceManager traceManager;
	final private String traceCategory;
	final private TraceCallable<RESULT> callable;
	
	public FutureTask(TraceManager traceManager,Trace scheduleTrace,String traceCategory,TraceCallable<RESULT> callable)
	{
		this.traceCategory=traceCategory;
		this.scheduleTrace=scheduleTrace;
		this.traceManager=traceManager;
		this.callable=(TraceCallable<RESULT>)callable;
		this.status=status.READY;
	}

	@SuppressWarnings("unchecked")
	void execute()
	{
		synchronized (this)
		{
			if (this.status!=TaskStatus.READY)
			{
				return;
			}
			this.status=TaskStatus.EXECUTING;
		}
		RESULT result=null;
		Throwable throwable=null;
		try (Trace trace=new Trace(this.traceManager,this.scheduleTrace,this.traceCategory,false))
		{
			try
			{
				result=this.callable.call(trace);
			}
			catch (Throwable t)
			{
				throwable=t;
//				t.printStackTrace();
				trace.close(t);
			}
		}
		synchronized (this)
		{
			this.result=result;
			this.throwable=throwable;
			this.status=TaskStatus.COMPLETED;
			this.notifyAll();
		}
	}
	public TaskStatus getExecutableStatus()
	{
		synchronized(this)
		{
			return this.status;
		}
	}
	public boolean waitForCompletion(long timeout)
	{
		synchronized(this)
		{
			return Synchronization.waitForNoThrow(this, ()->{return this.status==TaskStatus.COMPLETED;},timeout);
		}
	}
	public void waitForCompletion()
	{
		synchronized(this)
		{
			Synchronization.waitForNoThrow(this, ()->{return this.status==TaskStatus.COMPLETED;});
		}
	}
	
	public RESULT get() throws Throwable
	{
		synchronized(this)
		{
			if (this.throwable!=null)
			{
				throw this.throwable;
			}
			return this.result;
		}
	}

	public RESULT getResult()
	{
		synchronized(this)
		{
			return this.result;
		}
	}
	public Throwable getThrowable()
	{
		synchronized(this)
		{
			return this.throwable;
		}
	}

}
