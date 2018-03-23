package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class map extends GlobalEventTagElement<map>
{
    public map()
    {
        super("map");
    }
    
    public map name(String text)
    {
        return attr("name",text);
    }
    
}
