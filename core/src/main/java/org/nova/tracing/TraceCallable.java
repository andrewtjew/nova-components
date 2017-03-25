package org.nova.tracing;

public interface TraceCallable<RESULT>
{
	public RESULT call(Trace parent) throws Throwable;
}
