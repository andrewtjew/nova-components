package org.nova.collections;

import org.nova.tracing.Trace;

public interface Expire<KEY,VALUE>
{
	public void expire(Trace parent,KEY key,VALUE value) throws Throwable;
}
