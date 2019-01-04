package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.wrap;

public class textarea extends InputElement<textarea>
{
    public textarea()
    {
        super("textarea");
    }
    
    
    public textarea cols(int number)
    {
        return attr("cols",number);
    }
    public textarea dirname(String textareaname_dir)
    {
        return attr("dirname",textareaname_dir);
    }
    public textarea maxlength(int number)
    {
        return attr("maxlength",number);
    }
    public textarea placeholder(String text)
    {
        return attr("placeholder",text);
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
