package org.nova.net.printing;

public class Padding
{
    final private Size top;
    final private Size right;
    final private Size bottom;
    final private Size left;
    
    public Padding(Size top,Size right,Size bottom,Size left)
    {
        this.top=top;
        this.right=right;
        this.bottom=bottom;
        this.left=left;
    }

    public Padding(float top,float right,float  bottom,float left,Unit unit)
    {
        this(new Size(top,unit),new Size(right,unit),new Size(bottom,unit),new Size(left,unit));
    }

    public Padding(float size,Unit unit)
    {
        this(new Size(size,unit),new Size(size,unit),new Size(size,unit),new Size(size,unit));
    }
}
