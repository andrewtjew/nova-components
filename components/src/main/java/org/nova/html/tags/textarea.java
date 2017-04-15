package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.wrap;

public class textarea extends GlobalEventTagElement<textarea>
{
    public textarea()
    {
        super("textarea");
    }
    
    public textarea autofocus()
    {
        return attr("autofocus");
    }
    public textarea autofocus(boolean autofocus)
    {
        if (autofocus)
        {
            return attr("autofocus");
        }
        return this;
    }
    public textarea cols(int number)
    {
        return attr("cols",number);
    }
    public textarea dirname(String textareaname_dir)
    {
        return attr("dirname",textareaname_dir);
    }
    public textarea disabled()
    {
        return attr("disabled");
    }
    public textarea disabled(boolean disabled)
    {
        if (disabled)
        {
            return attr("disabled");
        }
        return this;
    }
    public textarea form(String form_id)
    {
        return attr("form",form_id);
    }
    public textarea maxlength(int number)
    {
        return attr("maxlength",number);
    }
    public textarea name(String text)
    {
        return attr("name",text);
    }
    public textarea placeholder(String text)
    {
        return attr("placeholder",text);
    }
    public textarea readonly()
    {
        return attr("readonly");
    }
    public textarea readonly(boolean readonly)
    {
        if (readonly)
        {
            return attr("readonly");
        }
        return this;
    }
    public textarea required()
    {
        return attr("required");
    }
    public textarea required(boolean required)
    {
        if (required)
        {
            return attr("required");
        }
        return this;
    }
    public textarea rows(int number)
    {
        return attr("rows",number);
    }
    public textarea wrap(wrap wrap)
    {
        return attr("wrap",wrap);
    }
    
}
