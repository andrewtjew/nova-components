package org.nova.frameworks;

import org.nova.logging.Logger;
import org.nova.logging.Loggers;
import org.nova.logging.SourceQueueLogger;
import org.nova.tracing.TraceManager;

public class MinimalApplication
{
	final protected TraceManager traceManager;
	final private SourceQueueLogger logger;
	
	public MinimalApplication() throws Throwable
	{
		this.logger=Loggers.createConsoleLogger();
		this.traceManager=new TraceManager(this.logger);
		this.traceManager.setCaptureStackTrace(true);
	}
	
	public TraceManager getTraceManager()
	{
		return traceManager;
	}

	public SourceQueueLogger getLogger()
	{
		return this.logger;
	}

}
