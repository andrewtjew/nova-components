package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_range extends InputElement<input_range>
{
    public input_range()
    {
        super();
        attr("type","range");
    }

    public input_range autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input_range max(long number)
    {
        return attr("max",number);
    }
    public input_range min(long number)
    {
        return attr("min",number);
    }
    public input_range step(int number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    
}
