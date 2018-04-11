package org.nova.html.attributes;

public class ColorRect
{
    final private String value;
    
    public ColorRect(Color top,Color right,Color bottom,Color left)
    {
        if (right!=null)
        {
            if (bottom!=null)
            {
                if (left!=null)
                {
                    this.value=top+" "+right+" "+bottom+" "+left;
                    return;
                }
                this.value=top+" "+right+" "+bottom; 
                return;
            }
            this.value=top+" "+right; 
            return;
        }
        this.value=top.toString(); 
    }
    public ColorRect(Color top,Color right,Color bottom)
    {
        this(top,right,bottom,null);
    }
    public ColorRect(Color top,Color right)
    {
        this(top,right,null);
    }
    public ColorRect(Color value)
    {
        this(value,null);
    }
    @Override
    public String toString()
    {
        return this.value;
    }
    
}
