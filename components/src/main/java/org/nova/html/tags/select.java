package org.nova.html.tags;

import org.nova.html.elements.InputElement;

public class select extends InputElement<select>
{
    public select()
    {
        super("select");
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
