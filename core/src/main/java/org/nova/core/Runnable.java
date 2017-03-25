package org.nova.core;

public interface Runnable extends Callable<Void>
{
	public void run() throws Throwable;
	
	default Void call() throws Throwable
	{
		run();
		return null;
	}
}
