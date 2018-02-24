package org.nova.metrics;

public class SourceEvent
{
    final Object state;
    final long instantMs;
    final StackTraceElement[] stackTrace;
    final int stackTraceStartIndex;
    SourceEvent(Object state,StackTraceElement[] stackTrace,int stackTraceStartIndex)
    {
        this.state=state;
        this.stackTrace=stackTrace;
        this.stackTraceStartIndex=stackTraceStartIndex;
        this.instantMs=System.currentTimeMillis();
    }

    public Object getState()
    {
        return state;
    }

    public long getInstantMs()
    {
        return instantMs;
    }
    public StackTraceElement[] getStackTrace()
    {
        return this.stackTrace;
    }
    public int getStackTraceStartIndex()
    {
        return this.stackTraceStartIndex;
    }
    
    
}
