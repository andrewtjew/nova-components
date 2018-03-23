package org.nova.concurrent;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nova.logging.Logger;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceCallable;
import org.nova.tracing.TraceManager;
import org.nova.tracing.TraceRunnable;

public class MultiTaskScheduler
{
	private final ExecutorService executorService;
	private final TraceManager traceManager;
	private long number;
	private final HashMap<Long,Progress<?>> futures;
	private final Logger logger;
	
	public MultiTaskScheduler(TraceManager traceManager,ExecutorService executorService,Logger logger)
	{
		this.traceManager=traceManager;
		this.executorService=executorService;
		this.futures=new HashMap<>();
		this.logger=logger;
	}
    public MultiTaskScheduler(TraceManager traceManager,int maximumThreads,Logger logger)
    {
        this(traceManager,Executors.newFixedThreadPool(maximumThreads),logger);
    }

	class Runner implements java.lang.Runnable
	{
		final Progress<?> future;
		final Task<?> task;

		Runner(Progress<?> futures,Task<?> task)
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
	
	private void complete(Progress<?> futures)
	{
		synchronized(this)
		{
			if (futures.completeTask())
			{
				this.futures.remove(futures.getNumber());
			}
		}		
	}
	
    public <RESULT> Progress<RESULT> schedule(Trace parent,String traceCategory,TraceCallable<RESULT>...callables)
	{
		Progress<RESULT> future=null;
		synchronized(this)
		{
			long number=this.number++;
			future=new Progress<RESULT>(this.traceManager,parent,traceCategory,number,callables,this.logger);
			this.futures.put(number, future);
		}
		for (int i=0;i<callables.length;i++)
		{
			this.executorService.submit(new Runner(future, future.getTask(i)));
		}
		return future;
	}

	public Progress<Void> schedule(Trace parent,String traceCategory,TraceRunnable...runnables)
    {
        Progress<Void> future=null;
        synchronized(this)
        {
            long number=this.number++;
            future=new Progress<Void>(this.traceManager,parent,traceCategory,number,runnables,this.logger);
            this.futures.put(number, future);
        }
        for (int i=0;i<runnables.length;i++)
        {
            this.executorService.submit(new Runner(future, future.getTask(i)));
        }
        return future;
    }


	public Progress<?>[] getProgressSnapshot()
	{
		synchronized(this)
		{
			return this.futures.values().toArray(new Progress[this.futures.size()]);
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
