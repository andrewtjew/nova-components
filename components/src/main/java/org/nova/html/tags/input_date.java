package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_date extends InputElement<input_date>
{
    public input_date()
    {
        super();
        attr("type","date");
    }
    public input_date autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input_date autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
        public input_date max(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("max",date);
    }
    public input_date max(long number)
    {
        return attr("max",number);
    }
    public input_date min(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("min",date);
    }
    public input_date min(long number)
    {
        return attr("min",number);
    }
    public input_date pattern(String regex) //text, date, search, url, tel, email, and password.
    {
        return attr("pattern",regex);
    }
    public input_date step(int number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    public input_date required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_date required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public input_date value(String text) 
    {
        return attr("value",text);
    }
    
}
