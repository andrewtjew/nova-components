package org.nova.html.properties;

public class Size
{
    final private String size;
    public Size(int size,unit unit)
    {
        this.size=size+unit.toString();
    }
    public Size(double size,unit unit)
    {
        this.size=size+unit.toString();
    }
    @Override
    public String toString()
    {
        return this.size;
    }
}
