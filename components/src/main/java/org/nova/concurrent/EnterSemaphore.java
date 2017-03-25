package org.nova.concurrent;

public class EnterSemaphore implements AutoCloseable
{
	final private long start;
	final Semaphore semaphore;
	public EnterSemaphore(Semaphore semaphore)
	{
		this.semaphore=semaphore;
		this.start=semaphore.enter();
	}
	@Override
	public void close()
	{
		this.semaphore.leave(start);
	}
}
