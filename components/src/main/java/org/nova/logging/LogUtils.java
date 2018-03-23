package org.nova.logging;

import org.nova.flow.SourceQueue;
import org.nova.flow.SourceQueueConfiguration;
import org.nova.logging.LogEntry;

public class LogUtils
{
	public static SourceQueueLogger createConsoleLogger(String category,Formatter formatter,SourceQueueConfiguration configuration,boolean outputSegments) throws Throwable
	{
		SourceQueue<LogEntry> queue=new SourceQueue<>(new ConsoleWriter(formatter,outputSegments),configuration);
		queue.start();
		return new SourceQueueLogger(0,category,queue);
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
        return new SourceQueueLogger(0,category,queue);
    }

    public static SourceQueueLogger createSimpleFileLogger(LogDirectoryManager logDirectoryManager,String category) throws Throwable
    {
        return createSimpleFileLogger(logDirectoryManager,category, new JSONFormatter(), new SourceQueueConfiguration());
    }

    public static SourceQueueLogger createSimpleFileLogger(LogDirectoryManager logDirectoryManager) throws Throwable
    {
        return createSimpleFileLogger(logDirectoryManager,null);
    }

    public static String toString(Formatter formatter,LogEntry[] logEntries,boolean document) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        if (document)
        {
            sb.append(formatter.beginDocument());
        }
        for (LogEntry logEntry:logEntries)
        {
            sb.append(formatter.format(logEntry));
        }
        if (document)
        {
            sb.append(formatter.endDocument());
        }
        return sb.toString();
        
    }
	
}
