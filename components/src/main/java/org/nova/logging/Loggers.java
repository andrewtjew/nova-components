package org.nova.logging;

import org.nova.flow.SourceQueue;
import org.nova.flow.SourceQueueConfiguration;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;

public class Loggers
{
	public static SourceQueueLogger createConsoleLogger(String category,Formatter formatter,SourceQueueConfiguration configuration,boolean outputSegments) throws Throwable
	{
		SourceQueue<LogEntry> queue=new SourceQueue<>(new ConsoleWriter(formatter,outputSegments),configuration);
		queue.start();
		return new SourceQueueLogger(category,queue);
	}

	public static SourceQueueLogger createConsoleLogger(String category) throws Throwable
	{
		return createConsoleLogger(category, new JSONFormatter(), new SourceQueueConfiguration(),false);
	}

	public static SourceQueueLogger createConsoleLogger() throws Throwable
	{
		return createConsoleLogger(null);
	}

	
    public static SourceQueueLogger createSimpleFileLogger(LogDirectoryManager logDirectoryManager,String category,Formatter formatter,SourceQueueConfiguration configuration) throws Throwable
    {
        SourceQueue<LogEntry> queue=new SourceQueue<>(new SimpleFileWriter(logDirectoryManager,formatter),configuration);
        queue.start();
        return new SourceQueueLogger(category,queue);
    }

    public static SourceQueueLogger createSimpleFileLogger(LogDirectoryManager logDirectoryManager,String category) throws Throwable
    {
        return createSimpleFileLogger(logDirectoryManager,category, new JSONFormatter(), new SourceQueueConfiguration());
    }

    public static SourceQueueLogger createSimpleFileLogger(LogDirectoryManager logDirectoryManager) throws Throwable
    {
        return createSimpleFileLogger(logDirectoryManager,null);
    }

	
}
