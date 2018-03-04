package org.nova.logging;

import java.io.OutputStream;

import org.nova.logging.LogEntry;

public abstract class Formatter
{
	abstract public String beginDocument() throws Throwable;
	abstract public String endDocument() throws Throwable;
	abstract public String format(LogEntry entry) throws Throwable;
}
