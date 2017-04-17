package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_search extends InputElement<input_search>
{
    public input_search()
    {
        super();
        attr("type","search");
    }
    
    public input_search autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input_search autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    
    public input_search pattern(String regex)
    {
        return attr("pattern",regex);
    }
    public input_search placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    
    public input_search size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public input_search required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_search required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    
}
