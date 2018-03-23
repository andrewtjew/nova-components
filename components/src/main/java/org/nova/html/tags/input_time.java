package org.nova.html.tags;

import org.nova.html.elements.InputElement;

public class input_time extends InputElement<input_time>
{
    public input_time()
    {
        super();
        attr("type","time");
    }

    public input_time max(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("max",date);
    }
    public input_time min(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("min",date);
    }
    public input_time step(int number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    public input_time required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_time required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    
}
