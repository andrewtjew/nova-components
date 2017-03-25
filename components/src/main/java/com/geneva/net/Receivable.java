package com.geneva.net;

import org.nova.tracing.Trace;

public interface Receivable
{
	public void process(Trace parent,Context context) throws Throwable;
}
