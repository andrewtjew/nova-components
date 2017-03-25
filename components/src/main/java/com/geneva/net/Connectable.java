package com.geneva.net;

import org.nova.tracing.Trace;

public interface Connectable
{
	public boolean accept(Trace parent,ClientConnection connection) throws Throwable;
	public void close(Trace parent,ClientConnection connection) throws Throwable;
	
}
