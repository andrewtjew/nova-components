package org.nova.html.attributes;

public class Color
{
    final private String color;
    public Color(String color)
    {
        this.color=color;
    }
    static public Color rgba(int red,int green,int blue,float opacity)
    {
        return new Color("rgba("+red+","+green+","+blue+","+opacity+")");
    }
    static public Color rgb(int red,int green,int blue)
    {
        return new Color("rgb("+red+","+green+","+blue+")");
    }
    static public Color hsla(int hue,int saturation,int lightness,float opacity)
    {
        return new Color("hsla("+hue+","+saturation+"%,"+lightness+"%,"+opacity+")");
    }
    static public Color hsl(int hue,int saturation,int lightness)
    {
        return new Color("hsl("+hue+","+saturation+"%,"+lightness+"%)");
    }
    
    @Override
    public String toString()
    {
        return this.color;
    }
}
