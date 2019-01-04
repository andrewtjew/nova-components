package org.nova.core;

// For passing back reference results as in: void func(IntBox result) { result.set(42);}

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
