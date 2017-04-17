package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_datetime_local extends InputElement<input_datetime_local>
{
    public input_datetime_local()
    {
        super();
        attr("type","datetime-local");
    }
    public input_datetime_local autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input_datetime_local autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    public input_datetime_local max(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("max",date);
    }
    public input_datetime_local max(long number)
    {
        return attr("max",number);
    }
    public input_datetime_local maxlength(long number) //text
    {
        return attr("maxlength",number);
    }
    public input_datetime_local min(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("min",date);
    }
    public input_datetime_local min(long number)
    {
        return attr("min",number);
    }
    public input_datetime_local step(int number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    public input_datetime_local width(String pixels) //image
    {
        return attr("width",pixels);
    }
    public input_datetime_local required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_datetime_local required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    
}
