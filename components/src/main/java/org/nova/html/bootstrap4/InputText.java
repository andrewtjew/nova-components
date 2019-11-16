package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;

public class InputText extends InputComponent<InputText>
{
    public InputText()
    {
        super("input","text");
    }
    public InputText autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        attr("autocomplete",autocomplete);
        return this;
    }
    public InputText autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    
    public InputText dirname(String value) //text
    {
        return attr("dirname",value);
    }
    public InputText maxlength(long number) //text
    {
        return attr("maxlength",number);
    }
    public InputText pattern(String regex) 
    {
        return attr("pattern",regex);
    }
    public InputText placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    public InputText list(String id)
    {
        return attr("list",id);
    }
    
    public InputText size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public InputText required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public InputText value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }

}

