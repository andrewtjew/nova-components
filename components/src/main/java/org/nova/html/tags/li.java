package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class li extends GlobalEventTagElement<li>
{
    public li()
    {
        super("li");
    }
    
    public li value(int value)
    {
        return attr("value",value);
    }
    
}
