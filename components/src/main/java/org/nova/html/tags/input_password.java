package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_password extends InputElement<input_password>
{
    public input_password()
    {
        super();
        attr("type","password");
    }

    public input_password autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input_password autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    public input_password pattern(String regex)
    {
        return attr("pattern",regex);
    }
    public input_password placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    
    public input_password size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public input_password required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_password required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public input_password value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
 
}
