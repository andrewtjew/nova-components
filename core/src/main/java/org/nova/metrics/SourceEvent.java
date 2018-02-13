package org.nova.metrics;

public class SourceEvent
{
    final Object state;
    final long instantMs;
    final String source;
    SourceEvent(Object state,String source)
    {
        this.state=state;
        this.source=source;
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
    public String getSource()
    {
        return this.source;
    }
    
}
