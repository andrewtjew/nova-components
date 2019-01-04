package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class param extends GlobalEventTagElement<param>
{
    public param()
    {
        super("param",true);
    }
    
    public param name(String name)
    {
        return attr("name",name);
    }
    public param value(String value)
    {
        return attr("value",value);
    }
    
}
