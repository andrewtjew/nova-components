package org.nova.html.xtags;

import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.properties.Size;
import org.nova.html.properties.Style;
import org.nova.html.properties.position;
import org.nova.html.properties.unit;
import org.nova.html.tags.div;

public class center_div extends GlobalEventTagElement<center_div>
{
    public center_div(Size width)
    {
        super("div");
        style(new Style().position(position.relative).left(new Size(50,unit.percent)).width(width).margin_left(new Size(-width.value()/2,width.unit())));
    }
    
}
