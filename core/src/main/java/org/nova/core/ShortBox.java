package org.nova.core;

public class ShortBox
{
    private short value;
    public ShortBox(short value)
    {
        this.value=value;
    }
    public ShortBox()
    {
        this((short)0);
    }
    public void set(short value)
    {
        this.value=value;
    }
    public short get()
    {
        return this.value;
    }
    
}
