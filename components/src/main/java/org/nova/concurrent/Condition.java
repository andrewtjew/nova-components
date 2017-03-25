package org.nova.concurrent;

import org.nova.core.NoThrowPredicate;
import org.nova.core.Predicate;

public class Condition
{
	public static void waitFor(Object synchronizationObject,Predicate predicate)  throws Throwable
	{
		while (predicate.evaluate()==false)
		{
			try
			{
				synchronizationObject.wait();
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	public static boolean waitFor(Object synchronizationObject,Predicate predicate,long timeout) throws Throwable
	{
		long start=System.currentTimeMillis();
		while (predicate.evaluate()==false)
		{
			if (System.currentTimeMillis()-start>timeout)
			{
				return false;
			}
			try
			{
				synchronizationObject.wait(timeout);
			}
			catch (InterruptedException e)
			{
			}
		}
		return true;
	}
	

	public static void waitForNoThrow(Object synchronizationObject,NoThrowPredicate predicate)
	{
		while (predicate.evaluate()==false)
		{
			try
			{
				synchronizationObject.wait();
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	public static boolean waitForNoThrow(Object synchronizationObject,NoThrowPredicate predicate,long timeout)
	{
		long start=System.currentTimeMillis();
		while (predicate.evaluate()==false)
		{
			if (System.currentTimeMillis()-start>timeout)
			{
				return false;
			}
			if (timeout>0)
			{
				try
				{
					synchronizationObject.wait(timeout);
				}
				catch (InterruptedException e)
				{
				}
			}
			else
			{
				try
				{
					synchronizationObject.wait();
				}
				catch (InterruptedException e)
				{
				}
			}
		}
		return true;
	}

}
