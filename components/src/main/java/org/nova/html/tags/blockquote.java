package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

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
