package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class option extends GlobalEventTagElement<option>
{
    public option()
    {
        super("option");
    }
    
    public option disabled()
    {
        return attr("disabled");
    }
    public option disabled(boolean disabled)
    {
        if (disabled)
        {
            return attr("disabled");
        }
        return this;
    }
    public option label(String text)
    {
        return attr("label",text);
    }
    public option selected()
    {
        return attr("selected");
    }
    public option selected(boolean selected)
    {
        if (selected)
        {
            return attr("selected");
        }
        return this;
    }
    public option value(String text)
    {
        return attr("value",text);
    }
    public option value(Object value)
    {
        return attr("value",value);
    }
    
}
