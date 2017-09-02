package org.nova.concurrent;

public class DelaySemaphore
{
    final private int max;
    private int count;
    private long last;
    
    public DelaySemaphore(int max)
    {
        this.max=max;
    }
    
    public boolean enter(long delay)
    {
        synchronized (this)
        {
            if (this.count>=this.max)
            {
                return false;
            }
            long now=System.currentTimeMillis();
            if (now-last<delay)
            {
                return false;
            }
            this.count++;
            return true;
        }
    }
    public boolean enter()
    {
        synchronized (this)
        {
            if (this.count>=this.max)
            {
                return false;
            }
            this.count++;
            return true;
        }
    }
    public void leave()
    {
        synchronized (this)
        {
            if (this.count>0)
            {
                this.count--;
            }
        }        
    }
    public int getCount()
    {
        synchronized(this)
        {
            return this.count;
        }
    }
}
