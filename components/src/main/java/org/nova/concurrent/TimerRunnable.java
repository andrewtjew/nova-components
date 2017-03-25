package org.nova.concurrent;

import org.nova.tracing.Trace;

public interface TimerRunnable
{
	public void run(Trace parent,TimerTask event) throws Throwable;
}
