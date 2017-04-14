package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class col extends GlobalEventTagElement<col>
{
    public col()
    {
        super("col");
    }
    
    public col span(int number)
    {
        return attr("span",number);
    }
}
