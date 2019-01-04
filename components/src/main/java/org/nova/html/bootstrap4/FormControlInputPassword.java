package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_password;

public class FormControlInputPassword extends FormControlInputComponent<FormControlInputPassword>
{
    public FormControlInputPassword()
    {
        super("password");
    }
    public FormControlInputPassword autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public FormControlInputPassword autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    public FormControlInputPassword pattern(String regex)
    {
        return attr("pattern",regex);
    }
    public FormControlInputPassword placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    
    public FormControlInputPassword size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public FormControlInputPassword required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public FormControlInputPassword required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public FormControlInputPassword value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
}

