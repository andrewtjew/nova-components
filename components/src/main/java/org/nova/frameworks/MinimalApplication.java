package org.nova.frameworks;

import org.nova.logging.LogUtils;
import org.nova.logging.Logger;
import org.nova.logging.SourceQueueLogger;
import org.nova.metrics.SourceEventBoard;
import org.nova.tracing.TraceManager;

public class MinimalApplication
{
	final protected TraceManager traceManager;
	final private Logger logger;
	final private SourceEventBoard statusBoard;
	
	public MinimalApplication() throws Throwable
	{
		this.logger=LogUtils.createConsoleLogger();
		this.traceManager=new TraceManager(this.logger);
		this.statusBoard=new SourceEventBoard();
	}
    public MinimalApplication(Logger logger) throws Throwable
    {
        this.logger=logger;
        this.traceManager=new TraceManager(this.logger);
        this.statusBoard=new SourceEventBoard();
    }
	
	public SourceEventBoard getStatusBoard()
	{
	    return this.statusBoard;
	}
	public TraceManager getTraceManager()
	{
		return traceManager;
	}

	public Logger getLogger()
	{
		return this.logger;
	}

}
