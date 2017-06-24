package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_number extends InputElement<input_number>
{
    public input_number()
    {
        super();
        attr("type","number");
    }
    public input_number max(double number)
    {
        return attr("max",number);
    }
    public input_number max(long number)
    {
        return attr("max",number);
    }
    public input_number min(double number)
    {
        return attr("min",number);
    }
    public input_number min(long number)
    {
        return attr("min",number);
    }
    public input_number step(double number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    public input_number step(long number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }

    public input_number required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_number required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public input_number value(double value)
    {
        return attr("value",value);
    }
    public input_number value(long value)
    {
        return attr("value",value);
    }
    
}
