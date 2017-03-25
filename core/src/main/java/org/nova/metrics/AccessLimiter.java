package org.nova.metrics;

public class AccessLimiter
{
    private long deniedCount;
    private long allowedCount;
    private int available;
    private long last;
    final private int maxAllowedPerInterval;
    final private long intervalNs;
    
    public AccessLimiter(int maxAllowedPerInterval,double allowedInterval,int firstAvailable)
    {
        this.available=firstAvailable;
        this.maxAllowedPerInterval=maxAllowedPerInterval;
        this.intervalNs=(long)(allowedInterval*1.0e9);
        this.last=System.nanoTime();
    }
    
    public boolean isAccessAllowedAndUpdate()
    {
        if (this.available>0)
        {
            this.available--;
            this.allowedCount++;
            return true;
        }
        long now=System.nanoTime();
        if (now-this.last<intervalNs)
        {
            this.deniedCount++;
            return false;
        }
        this.last=now;
        this.available=this.maxAllowedPerInterval-1;
        this.allowedCount++;
        return true;
    }

    public long getDeniedCount()
    {
        return deniedCount;
    }

    public long getAllowedCount()
    {
        return allowedCount;
    }
}
