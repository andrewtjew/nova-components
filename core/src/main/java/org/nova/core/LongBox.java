package org.nova.core;

public class LongBox
{
    private long value;
    public LongBox(long value)
    {
        this.value=value;
    }
    public LongBox()
    {
        this(0);
    }
    public void set(long value)
    {
        this.value=value;
    }
    public long get()
    {
        return this.value;
    }
    
}
