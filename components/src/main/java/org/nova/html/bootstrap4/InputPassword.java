package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_password;


public class InputPassword extends InputComponent<InputPassword>
{
    public InputPassword()
    {
        super("input","password");
    }
    public InputPassword autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public InputPassword autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    public InputPassword pattern(String regex)
    {
        return attr("pattern",regex);
    }
    public InputPassword placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    
    public InputPassword size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public InputPassword required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public InputPassword required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public InputPassword value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
}

