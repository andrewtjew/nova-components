package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_number;
import org.nova.html.tags.input_text;

public class FormControlInputNumber extends FormControlInputComponent<FormControlInputNumber>
{
    public FormControlInputNumber()
    {
        super("number");
    }
    public FormControlInputNumber max(double number)
    {
        return attr("max",number);
    }
    public FormControlInputNumber max(long number)
    {
        return attr("max",number);
    }
    public FormControlInputNumber min(double number)
    {
        return attr("min",number);
    }
    public FormControlInputNumber min(long number)
    {
        return attr("min",number);
    }
    public FormControlInputNumber step(double number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    public FormControlInputNumber step(long number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }

    public FormControlInputNumber required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public FormControlInputNumber required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public FormControlInputNumber value(Double value)
    {
        return attr("value",value);
    }
    public FormControlInputNumber value(double value)
    {
        return attr("value",value);
    }
    public FormControlInputNumber value(Long value)
    {
        return attr("value",value);
    }
    public FormControlInputNumber value(long value)
    {
        return attr("value",value);
    }
    public FormControlInputNumber value(Integer value)
    {
        return attr("value",value);
    }
    public FormControlInputNumber value(int value)
    {
        return attr("value",value);
    }
    public FormControlInputNumber size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public FormControlInputNumber placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    public FormControlInputNumber autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public FormControlInputNumber autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
}

