package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_email;
import org.nova.html.tags.select;

public class Select extends InputComponent<Select>
{
    public Select()
    {
        super("select","form-control",null);
    }
    
    public Select multiple()
    {
        return attr("multiple");
    }
    public Select multiple(boolean multiple)
    {
        if (multiple)
        {
            return attr("multiple");
        }
        return this;
    }
    public Select required()
    {
        return attr("required");
    }
    public Select required(boolean required)
    {
        if (required)
        {
            return attr("required");
        }
        return this;
    }
 }

