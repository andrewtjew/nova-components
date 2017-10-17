package org.nova.logging;

public class ThrowablesLog
{
    private InstantThrowable first;
    private InstantThrowable last; 
    
    public ThrowablesLog()
    {
    }
    
    public void log(Throwable throwable)
    {
        synchronized(this)
        {
            last=new InstantThrowable(throwable);
            if (first==null)
            {
                first=last;
            }
        }
    }

    public InstantThrowable getFirst()
    {
        synchronized(this)
        {
            return first;
        }
    }

    public InstantThrowable getLast()
    {
        synchronized(this)
        {
            return last;
        }
    }

}
