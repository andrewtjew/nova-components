package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_email extends InputElement<input_email>
{
    public input_email()
    {
        super();
        attr("type","email");
    }
    public input_email autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input_email autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    public input_email max(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("max",date);
    }
    public input_email max(long number)
    {
        return attr("max",number);
    }
    public input_email maxlength(long number) //text
    {
        return attr("maxlength",number);
    }
    public input_email min(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("min",date);
    }
    public input_email min(long number)
    {
        return attr("min",number);
    }
    public input_email step(int number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    public input_email width(String pixels) //image
    {
        return attr("width",pixels);
    }
    public input_email required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_email required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    
}
