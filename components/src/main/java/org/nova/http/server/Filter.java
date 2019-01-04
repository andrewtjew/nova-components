package org.nova.http.server;

import org.nova.tracing.Trace;

public abstract class Filter
{
    public abstract Response<?> executeNext(Trace trace,Context context,FilterChain filterChain) throws Throwable;
    public void onRegister(RequestHandler requestHandler)
    {
    }
	
}