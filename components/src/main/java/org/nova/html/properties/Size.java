package org.nova.html.properties;

public class Size
{
    final unit unit;
    final double value;
    public Size(int value,unit unit)
    {
        this.value=value;
        this.unit=unit;
    }
    public Size(double value,unit unit)
    {
        this.value=value;
        this.unit=unit;
    }
    @Override
    public String toString()
    {
        return this.value+this.unit.toString();
    }
    public double value()
    {
        return value;
    }
    public unit unit()
    {
        return unit;
    }
}
