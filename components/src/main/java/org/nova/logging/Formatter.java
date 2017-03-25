package org.nova.logging;

import java.io.OutputStream;

import org.nova.logging.LogEntry;

public abstract class Formatter
{
	abstract public long outputBegin(OutputStream outputStream) throws Throwable;
	abstract public long outputEnd(OutputStream outputStream) throws Throwable;
	abstract public long output(LogEntry entry,OutputStream outputStream) throws Throwable;
}
