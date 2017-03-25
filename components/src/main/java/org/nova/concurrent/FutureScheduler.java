package org.nova.concurrent;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nova.tracing.Trace;
import org.nova.tracing.TraceCallable;
import org.nova.tracing.TraceManager;

public class FutureScheduler
{
	private final ExecutorService executorService;
	private final TraceManager traceManager;
	private long number;
	private final HashMap<Long,Future<?>> futures;
	
	public FutureScheduler(TraceManager traceManager,ExecutorService executorService)
	{
		this.traceManager=traceManager;
		this.executorService=executorService;
		this.futures=new HashMap<>();
	}
	public FutureScheduler(TraceManager traceManager,int maximumThreads)
	{
		this(traceManager,Executors.newFixedThreadPool(maximumThreads));
	}

	class Runner implements java.lang.Runnable
	{
		final Future<?> future;
		final FutureTask<?> task;

		Runner(Future<?> futures,FutureTask<?> task)
		{
			this.task=task;
			this.future=futures;
		}

		@Override
		public void run()
		{
			this.task.execute();
			complete(future);
		}
	}
	
	private void complete(Future<?> futures)
	{
		synchronized(this)
		{
			if (futures.completeTask())
			{
				this.futures.remove(futures.getNumber());
			}
		}		
	}
	
	public <RESULT> Future<RESULT> schedule(Trace parent,String traceCategory,TraceCallable<RESULT>...callables)
	{
		Future<RESULT> future=null;
		synchronized(this)
		{
			long number=this.number++;
			future=new Future<RESULT>(this.traceManager,parent,traceCategory,number,callables);
			this.futures.put(number, future);
		}
		for (int i=0;i<callables.length;i++)
		{
			this.executorService.submit(new Runner(future, future.getCallableTask(i)));
		}
		return future;
	}

	public Future<?>[] getFutureSnapshot()
	{
		synchronized(this)
		{
			return this.futures.values().toArray(new Future[this.futures.size()]);
		}
	}
}
