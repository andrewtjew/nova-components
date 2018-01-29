package org.nova.core;

public class IntBox
{
    private int value;
    public IntBox(int value)
    {
        this.value=value;
    }
    public IntBox()
    {
        this(0);
    }
    public void set(int value)
    {
        this.value=value;
    }
    public int get()
    {
        return this.value;
    }
    
}
