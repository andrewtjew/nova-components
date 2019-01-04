package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_email;

public class FormControlInputEmail extends FormControlInputComponent<FormControlInputEmail>
{
    public FormControlInputEmail()
    {
        super("email");
    }
    public FormControlInputEmail autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public FormControlInputEmail autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    public FormControlInputEmail max(String date) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("max",date);
    }
    public FormControlInputEmail max(long number)
    {
        return attr("max",number);
    }
    public FormControlInputEmail maxlength(long number) //text
    {
        return attr("maxlength",number);
    }
    public FormControlInputEmail required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public FormControlInputEmail placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    public FormControlInputEmail required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public FormControlInputEmail value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }

}

