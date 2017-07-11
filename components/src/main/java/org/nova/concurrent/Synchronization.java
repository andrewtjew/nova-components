package org.nova.concurrent;

import org.nova.core.NoThrowPredicate;
import org.nova.core.Predicate;

public class Synchronization
{
    public static void wait(Object synchronizationObject)
    {
        try
        {
            synchronizationObject.wait();
        }
        catch (InterruptedException e)
        {
        }
    }
    public static void wait(Object synchronizationObject,long timeout)
    {
        try
        {
            synchronizationObject.wait(timeout);
            return;
        }
        catch (InterruptedException e)
        {
        }
    }
    
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
        if (timeout==Long.MAX_VALUE)
        {
            waitFor(synchronizationObject,predicate);
            return true;
        }
		long start=System.currentTimeMillis();
		while (predicate.evaluate()==false)
		{
            long remaining=timeout-(System.currentTimeMillis()-start);
            if (remaining<=0)
            {
                return false;
            }
			try
			{
				synchronizationObject.wait(remaining);
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
        if (timeout==Long.MAX_VALUE)
        {
            waitForNoThrow(synchronizationObject,predicate);
            return true;
        }
		long start=System.currentTimeMillis();
		while (predicate.evaluate()==false)
		{
            long remaining=timeout-(System.currentTimeMillis()-start);
            if (remaining<=0)
            {
                return false;
            }
			try
			{
				synchronizationObject.wait(remaining);
			}
			catch (InterruptedException e)
			{
			}
		}
		return true;
	}

	public static void sleep(Object synchronizationObject,long timeout)
    {
        long start=System.currentTimeMillis();
        for (;;)
        {
            long remaining=timeout-(System.currentTimeMillis()-start);
            if (remaining<=0)
            {
                return;
            }
            try
            {
                synchronizationObject.wait(remaining);
            }
            catch (InterruptedException e)
            {
            }
        }
    }

}
