package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.preload;

public class select extends GlobalEventTagElement<select>
{
    public select()
    {
        super("select");
    }
    
    public select autofocus()
    {
        return attr("autofocus");
    }
    public select autofocus(boolean autofocus)
    {
        if (autofocus)
        {
            return attr("autofocus");
        }
        return this;
    }
    public select disabled()
    {
        return attr("disabled");
    }
    public select disabled(boolean disabled)
    {
        if (disabled)
        {
            return attr("disabled");
        }
        return this;
    }
    public select form(String form_id)
    {
        return attr("form",form_id);
    }
    public select multiple()
    {
        return attr("multiple");
    }
    public select multiple(boolean multiple)
    {
        if (multiple)
        {
            return attr("multiple");
        }
        return this;
    }
    public select name(String text)
    {
        return attr("name",text);
    }
    public select required()
    {
        return attr("required");
    }
    public select required(boolean required)
    {
        if (required)
        {
            return attr("required");
        }
        return this;
    }
    public select size(int number)
    {
        return attr("size",number);
    }
    
}
