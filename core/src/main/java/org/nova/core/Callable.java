package org.nova.core;

public interface Callable<RESULT>
{
	public RESULT call() throws Throwable;
}
