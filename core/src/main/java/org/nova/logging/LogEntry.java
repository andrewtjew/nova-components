package org.nova.logging;

import org.nova.tracing.Trace;

public class LogEntry
{
	private final Level logLevel;
	private final long number;
	private final long created;
	private final String message;
	private final Throwable exception;
	private final Trace trace;
	private final Item[] items;
	private final String category;
	LogEntry(long number,String category,Level logLevel,long created,Throwable exception,Trace trace,String message,Item[] items)
	{
		this.number=number;
		this.category=category;
		this.logLevel=logLevel;
		this.created=created;
		this.message=message;
		this.exception=exception;
		this.trace=trace;
		this.items=items;
	}
	public long getNumber()
	{
		return number;
	}
	public Level getLogLevel()
	{
		return logLevel;
	}
	public long getCreated()
	{
		return created;
	}
	public String getMessage()
	{
		return message;
	}
	public Throwable getException()
	{
		return exception;
	}
	public Trace getTrace()
	{
		return trace;
	}
	public Item[] getItems()
	{
		return items;
	}
	public String getCategory()
	{
		return this.category;
	}
}
