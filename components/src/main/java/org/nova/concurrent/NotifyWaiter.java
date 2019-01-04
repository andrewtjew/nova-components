package org.nova.concurrent;

public class NotifyWaiter
{
	final private Object synchronizationObject;
	final private long timeout;
	final private long start; 
	public NotifyWaiter(Object synchronizationObject,long timeout)
	{
		this.synchronizationObject=synchronizationObject;
		this.timeout=timeout;
		this.start=System.currentTimeMillis();
	}
	
	public boolean waitForOldNotify()
	{
		if (System.currentTimeMillis()-this.start<this.timeout)
		{
			try
			{
				synchronizationObject.wait(this.timeout);
			}
			catch (InterruptedException e)
			{
				return false;
			}
			return System.currentTimeMillis()-this.start>=this.timeout;
		}
		return true;
	}
    public boolean waitForNewNotify()
    {
        if (System.currentTimeMillis()-this.start<this.timeout)
        {
            try
            {
                synchronizationObject.wait(this.timeout);
            }
            catch (InterruptedException e)
            {
                return false;
            }
            return System.currentTimeMillis()-this.start<this.timeout;
        }
        return true;
    }
}
