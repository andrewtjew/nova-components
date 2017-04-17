package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_text extends InputElement<input_text>
{
    public input_text()
    {
        super();
        attr("type","text");
    }
    
    public input_text autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input_text autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    
    public input_text dirname(String value) //text
    {
        return attr("dirname",value);
    }
    public input_text maxlength(long number) //text
    {
        return attr("maxlength",number);
    }
    public input_text pattern(String regex) 
    {
        return attr("pattern",regex);
    }
    public input_text placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    
    public input_text size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public input_text required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_text value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }

    
}
