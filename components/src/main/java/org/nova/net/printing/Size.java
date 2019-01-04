package org.nova.net.printing;

public class Size
{
    final public float value;
    final public Unit unit;
    
    public static Size ZERO=new Size(0,Unit.Point);
    
    public Size(float value,Unit unit)
    {
        this.value=value;
        this.unit=unit;
    }
    static public Size Point(float value)
    {
        return new Size(value,Unit.Point);
    }
    static public Size Percent(float value)
    {
        return new Size(value,Unit.Percent);
    }
    static public Size Em(float value)
    {
        return new Size(value,Unit.Em);
    }
    
}
