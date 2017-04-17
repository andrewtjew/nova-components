package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_url extends InputElement<input_url>
{
    public input_url()
    {
        super();
        attr("type","url");
    }

    public input_url autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input_url autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    
    public input_url pattern(String regex) 
    {
        return attr("pattern",regex);
    }
    public input_url placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    
    public input_url size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public input_url required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_url required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
   
}
