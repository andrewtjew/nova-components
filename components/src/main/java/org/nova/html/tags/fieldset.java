package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.audio_preload;

public class fieldset extends GlobalEventTagElement<fieldset>
{
    public fieldset()
    {
        super("fieldset");
    }
    
    public fieldset disabled()
    {
        return attr("disabled");
    }
    public fieldset disabled(boolean disabled)
    {
        if (disabled)
        {
            return attr("disabled");
        }
        return this;
    }
    public fieldset form(String form_id)
    {
        return attr("form",form_id);
    }
    public fieldset name(String text)
    {
        return attr("name",text);
    }
    
}
