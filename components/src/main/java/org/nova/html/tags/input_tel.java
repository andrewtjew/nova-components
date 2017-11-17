package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_tel extends InputElement<input_tel>
{
    public input_tel()
    {
        super();
        attr("type","tel");
    }

    public input_tel autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input_tel autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    
    public input_tel placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    
    public input_tel size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public input_tel required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_tel required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public input_tel value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
}
