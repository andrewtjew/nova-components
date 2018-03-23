package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class progress extends GlobalEventTagElement<progress>
{
    public progress()
    {
        super("progress");
    }
    
    public progress max(int number)
    {
        return attr("max",number);
    }
    public progress value(int number)
    {
        return attr("value",number);
    }
    
}
