package org.nova.concurrent;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nova.logging.Logger;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceCallable;
import org.nova.tracing.TraceManager;
import org.nova.tracing.TraceRunnable;

public class FutureScheduler
{
	private final ExecutorService executorService;
	private final TraceManager traceManager;
	private long number;
	private final HashMap<Long,Future<?>> futures;
	private final Logger logger;
	
	public FutureScheduler(TraceManager traceManager,ExecutorService executorService,Logger logger)
	{
		this.traceManager=traceManager;
		this.executorService=executorService;
		this.futures=new HashMap<>();
		this.logger=logger;
	}
    public FutureScheduler(TraceManager traceManager,int maximumThreads,Logger logger)
    {
        this(traceManager,Executors.newFixedThreadPool(maximumThreads),logger);
    }
    /*
    public FutureScheduler(TraceManager traceManager,int maximumThreads)
    {
        this(traceManager,Executors.newFixedThreadPool(maximumThreads),null);
    }
    */

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
		    this.future.startTask();
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
			future=new Future<RESULT>(this.traceManager,parent,traceCategory,number,callables,this.logger);
			this.futures.put(number, future);
		}
		for (int i=0;i<callables.length;i++)
		{
			this.executorService.submit(new Runner(future, future.getCallableTask(i)));
		}
		return future;
	}

	public Future<Void> schedule(Trace parent,String traceCategory,TraceRunnable...runnables)
    {
        Future<Void> future=null;
        synchronized(this)
        {
            long number=this.number++;
            future=new Future<Void>(this.traceManager,parent,traceCategory,number,runnables,this.logger);
            this.futures.put(number, future);
        }
        for (int i=0;i<runnables.length;i++)
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
	
	public void stop()
	{
        synchronized(this)
        {
            this.executorService.shutdownNow();
        }
	}
}
