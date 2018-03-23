package org.nova.frameworks;

import org.nova.logging.LogUtils;
import org.nova.logging.SourceQueueLogger;
import org.nova.metrics.SourceEventEventBoard;
import org.nova.tracing.TraceManager;

public class MinimalApplication
{
	final protected TraceManager traceManager;
	final private SourceQueueLogger logger;
	final private SourceEventEventBoard statusBoard;
	
	public MinimalApplication() throws Throwable
	{
		this.logger=LogUtils.createConsoleLogger();
		this.traceManager=new TraceManager(this.logger);
		this.statusBoard=new SourceEventEventBoard();
	}
	
	public SourceEventEventBoard getStatusBoard()
	{
	    return this.statusBoard;
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
