
package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.type;

public class ol extends GlobalEventTagElement<ol>
{
    public ol()
    {
        super("ol");
    }
    
    public ol reversed()
    {
        return attr("reversed");
    }
    public ol reversed(boolean reversed)
    {
        if (reversed)
        {
            return attr("reversed");
        }
        return this;
    }
    public ol start(int number)
    {
        return attr("start",number);
    }
    public ol type(type type)
    {
        return attr("type",type);
    }
    
}
