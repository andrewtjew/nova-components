package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.preload;

public class optgroup extends GlobalEventTagElement<optgroup>
{
    public optgroup()
    {
        super("optgroup");
    }
    
    public optgroup disabled()
    {
        return attr("disabled");
    }
    public optgroup disabled(boolean disabled)
    {
        if (disabled)
        {
            return attr("disabled");
        }
        return this;
    }
    public optgroup label(String text)
    {
        return attr("label",text);
    }
    
}
