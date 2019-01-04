package org.nova.net.printing;

public class Color
{
    final public float red;
    final public float green;
    final public float blue;
    final public float alpha;
    
    public Color(float red,float green,float blue,float alpha)
    {
        this.red=red;
        this.green=green;
        this.blue=blue;
        this.alpha=alpha;
    }
    public Color(float red,float green,float blue)
    {
        this(red,green,blue,0.0f);
    }
    public Color(float lightNess)
    {
        this(lightNess,lightNess,lightNess,1.0f);
    }
}
