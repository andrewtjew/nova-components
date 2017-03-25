package org.nova.tracing;

public interface TraceRunnable extends TraceCallable<Void>
{
	public void run(Trace parent) throws Throwable;
	
	default Void call(Trace parent) throws Throwable
	{
		run(parent);
		return null;
	}
}