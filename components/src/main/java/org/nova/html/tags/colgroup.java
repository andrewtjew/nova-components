package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class colgroup extends GlobalEventTagElement<colgroup>
{
    public colgroup()
    {
        super("colgroup");
    }
    
    public colgroup span(int number)
    {
        return attr("span",number);
    }
}
