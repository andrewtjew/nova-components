package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_email;

public class InputEmail extends InputComponent<InputEmail>
{
    public InputEmail()
    {
        super("input","email");
    }
    public InputEmail autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public InputEmail autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    public InputEmail max(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("max",date);
    }
    public InputEmail max(long number)
    {
        return attr("max",number);
    }
    public InputEmail maxlength(long number) //text
    {
        return attr("maxlength",number);
    }
    public InputEmail required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public InputEmail placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    public InputEmail required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public InputEmail value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }

}

