package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_email;
import org.nova.html.tags.select;

public class FormControlSelect extends InputComponent<FormControlSelect>
{
    public FormControlSelect()
    {
        super("select","form-control",null);
    }
    
    public FormControlSelect multiple()
    {
        return attr("multiple");
    }
    public FormControlSelect multiple(boolean multiple)
    {
        if (multiple)
        {
            return attr("multiple");
        }
        return this;
    }
    public FormControlSelect required()
    {
        return attr("required");
    }
    public FormControlSelect required(boolean required)
    {
        if (required)
        {
            return attr("required");
        }
        return this;
    }
 }

