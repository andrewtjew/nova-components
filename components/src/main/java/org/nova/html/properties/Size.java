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
    
    /*
    static public Size rem(double size)
    {
    	return new Size(size,org.nova.html.properties.unit.rem);
    }
    static public Size px(double size)
    {
        return new Size(size,org.nova.html.properties.unit.px);
    }
    */
}
