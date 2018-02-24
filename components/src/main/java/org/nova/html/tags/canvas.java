package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class canvas extends GlobalEventTagElement<canvas>
{
    public canvas()
    {
        super("canvas");
    }
    public canvas height(int pixels)
    {
        return attr("height",pixels);
    }
    public canvas width(int pixels)
    {
        return attr("width",pixels);
    }
    
    
}
