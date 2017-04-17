package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_week extends InputElement<input_week>
{
    public input_week()
    {
        super();
        attr("type","week");
    }

    public input_week max(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("max",date);
    }
    public input_week min(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("min",date);
    }
    public input_week step(int number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    public input_week required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_week required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    
}
