package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.LinkElement;
import org.nova.html.enums.target;
import org.nova.html.enums.crossorigin;
import org.nova.html.enums.link_rel;

public class link extends LinkElement<link>
{
    public link()
    {
    }
    
    public link rel(link_rel rel)
    {
        return attr("rel",rel);
    }
    
}
