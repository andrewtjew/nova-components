package org.nova.html.tags;

import org.nova.html.elements.InputElement;

public class input_month extends InputElement<input_month>
{
    public input_month()
    {
        super();
        attr("type","month");
    }


    public input_month max(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("max",date);
    }
    public input_month min(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("min",date);
    }
    public input_month step(int number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }

    
}
