package org.nova.frameworks;

import java.util.HashMap;

import org.nova.concurrent.FutureScheduler;
import org.nova.concurrent.TimerScheduler;
import org.nova.configuration.Configuration;
import org.nova.flow.SourceQueue;
import org.nova.logging.JSONBufferedLZ4Queue;
import org.nova.logging.JSONBufferedLZ4QueueConfiguration;
import org.nova.logging.Level;
import org.nova.logging.LogDirectoryManager;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;
import org.nova.logging.SourceQueueLogger;
import org.nova.metrics.MeterManager;
import org.nova.tracing.TraceManager;

public class CoreApplication
{
	final protected TimerScheduler timerScheduler;
	final protected FutureScheduler futureScheduler;
	final protected TraceManager traceManager;
	final protected Configuration configuration;
	final protected MeterManager meterManager;
	final private Logger logger;
	final HashMap<String,SourceQueueLogger> loggers;
	final SourceQueue<LogEntry> logQueue;
	final private LogDirectoryManager logDirectoryManager;
	
	public CoreApplication(Configuration configuration) throws Throwable
	{
        this.configuration=configuration;

        String directory=configuration.getValue("Logger.logDirectory","logs");
		long maxFiles=configuration.getIntegerValue("Logger.logDirectory.maxFiles",100);
		long reserve=configuration.getLongValue("Logger.logDirectory.reserveSpace",100_000_000_000L);
		long maxDirectorySize=configuration.getLongValue("Logger.logDirectory.maxDirectorySize",10_000_000_000L);
		int maxMakeSpaceRetries=configuration.getIntegerValue("Logger.logDirectory.maxMakeSpaceRetries",10);
		this.logDirectoryManager=new LogDirectoryManager(directory, maxMakeSpaceRetries, maxFiles, maxDirectorySize, reserve);
		this.logQueue=new JSONBufferedLZ4Queue(logDirectoryManager, new JSONBufferedLZ4QueueConfiguration());
		this.loggers=new HashMap<>();
		SourceQueueLogger traceLogger=this.getLogger("tracing");
		this.loggers.put("trace", traceLogger);

        this.meterManager=new MeterManager();
		this.traceManager=new TraceManager(traceLogger);
		this.futureScheduler=new FutureScheduler(traceManager,configuration.getIntegerValue("FutureScheduler.threads",1000));
		this.timerScheduler=new TimerScheduler(traceManager, this.getLogger());
		this.timerScheduler.start();
		this.logger=getLogger("application");
	}
	
	public MeterManager getMeterManager()
	{
		return meterManager;
	}

	public FutureScheduler getFutureScheduler()
	{
		return futureScheduler;
	}

	public TraceManager getTraceManager()
	{
		return traceManager;
	}

	public Configuration getConfiguration()
	{
		return configuration;
	}

	public TimerScheduler getTimerScheduler()
	{
		return timerScheduler;
	}
	public SourceQueueLogger getLogger(String category) throws Throwable
	{
		synchronized (this.loggers)
		{
		    SourceQueueLogger logger=this.loggers.get(category);
			if (logger==null)
			{
				logger=new SourceQueueLogger(category, this.logQueue);
				this.loggers.put(category, logger);
			}
			return logger;
		}
	}
	public Logger getLogger() 
	{
		return this.logger;
	}
	public SourceQueue<LogEntry> getLogQueue()
	{
	    return this.logQueue;
	}
	public LogDirectoryManager getLogDirectoryManager()
	{
		return this.logDirectoryManager;
	}
	public void runForever() throws Throwable
	{
		Object object=new Object();
		this.getLogger().log(Level.NORMAL,"Started");
		synchronized (object)
		{
			for (;;)
			{
				try
				{
					object.wait();
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}
}
