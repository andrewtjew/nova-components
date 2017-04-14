package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.area_rel;
import org.nova.html.enums.area_shape;
import org.nova.html.enums.bdo_dir;
import org.nova.html.enums.target;

public class blockquote extends GlobalEventTagElement<blockquote>
{
    public blockquote()
    {
        super("blockquote");
    }
    
    public blockquote cite(String URL)
    {
        return attr("cite",URL);
    }
}
