package org.nova.html.xtags;

import org.nova.html.attributes.Size;
import org.nova.html.attributes.Style;
import org.nova.html.attributes.position;
import org.nova.html.attributes.unit;
import org.nova.html.elements.GlobalEventTagElement;

public class div_center extends GlobalEventTagElement<div_center>
{
    public div_center(Size width)
    {
        super("div");
        style(new Style().position(position.relative).left(new Size(50,unit.percent)).width(width).margin_left(new Size(-width.value()/2,width.unit())));
    }
    
}
