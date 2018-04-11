package org.nova.html.attributes;

public class BorderStyleRect
{
    final private String value;
    
    public BorderStyleRect(border_style top,border_style right,border_style bottom,border_style left)
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
    public BorderStyleRect(border_style top,border_style right,border_style bottom)
    {
        this(top,right,bottom,null);
    }
    public BorderStyleRect(border_style top,border_style right)
    {
        this(top,right,null);
    }
    public BorderStyleRect(border_style value)
    {
        this(value,null);
    }
    @Override
    public String toString()
    {
        return this.value;
    }
    
}
