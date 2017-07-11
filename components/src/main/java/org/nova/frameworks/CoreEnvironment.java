package org.nova.frameworks;

import java.util.HashMap;

import org.nova.concurrent.FutureScheduler;
import org.nova.concurrent.TimerScheduler;
import org.nova.configuration.Configuration;
import org.nova.flow.SourceQueue;
import org.nova.flow.SourceQueueConfiguration;
import org.nova.logging.ConsoleWriter;
import org.nova.logging.JSONBufferedLZ4Queue;
import org.nova.logging.JSONBufferedLZ4QueueConfiguration;
import org.nova.logging.JSONFormatter;
import org.nova.logging.Level;
import org.nova.logging.LogDirectoryManager;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;
import org.nova.logging.SimpleFileWriter;
import org.nova.logging.SourceQueueLogger;
import org.nova.metrics.MeterManager;
import org.nova.tracing.TraceManager;

public class CoreEnvironment
{
	final private TimerScheduler timerScheduler;
	final private FutureScheduler futureScheduler;
	final private TraceManager traceManager;
	final private Configuration configuration;
	final private MeterManager meterManager;
	final private Logger logger;
	final HashMap<String,SourceQueueLogger> loggers;
	final SourceQueue<LogEntry> logSourceQueue;
	final private LogDirectoryManager logDirectoryManager;
	
	public CoreEnvironment(Configuration configuration) throws Throwable
	{
        this.configuration=configuration;

        String directory=configuration.getValue("Logger.logDirectory","logs");
		long maxFiles=configuration.getIntegerValue("Logger.logDirectory.maxFiles",100);
		long reserve=configuration.getLongValue("Logger.logDirectory.reserveSpace",100_000_000_000L);
		long maxDirectorySize=configuration.getLongValue("Logger.logDirectory.maxDirectorySize",10_000_000_000L);
		int maxMakeSpaceRetries=configuration.getIntegerValue("Logger.logDirectory.maxMakeSpaceRetries",10);
		this.logDirectoryManager=new LogDirectoryManager(directory, maxMakeSpaceRetries, maxFiles, maxDirectorySize, reserve);

		String loggerType=configuration.getValue("Logger.class","JSONBufferedLZ4Queue");
		switch (loggerType)
		{
            case "SimpleFileWriter":
                this.logSourceQueue=new SourceQueue<LogEntry>(new SimpleFileWriter(this.logDirectoryManager,new JSONFormatter()),new SourceQueueConfiguration());
                break;

            case "JSONBufferedLZ4Queue":
                this.logSourceQueue=new JSONBufferedLZ4Queue(logDirectoryManager, new JSONBufferedLZ4QueueConfiguration());
                break;
                
		    default:
		        this.logSourceQueue=new SourceQueue<LogEntry>(new ConsoleWriter(new JSONFormatter(),true),new SourceQueueConfiguration());
		        break;
		}
		this.logSourceQueue.start();
		this.loggers=new HashMap<>();
		Logger traceLogger=this.getLogger("tracing");
	
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
				logger=new SourceQueueLogger(category, this.logSourceQueue);
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
	    return this.logSourceQueue;
	}
	public LogDirectoryManager getLogDirectoryManager()
	{
		return this.logDirectoryManager;
	}
}
