package org.nova.html.tags;

import org.nova.html.elements.LinkElement;

public class link_icon extends LinkElement<link_icon>
{
    public link_icon()
    {
        attr("rel","rel_icon");
    }
    
    public link_icon sizes(String sizes)
    {
        attr("sizes",sizes);
        return this;
    }
    
}
