package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;

public class FormControlInputText extends FormControlInputComponent<FormControlInputText>
{
    public FormControlInputText()
    {
        super("text");
    }
    public FormControlInputText autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        attr("autocomplete",autocomplete);
        return this;
    }
    public FormControlInputText autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
    
    public FormControlInputText dirname(String value) //text
    {
        return attr("dirname",value);
    }
    public FormControlInputText maxlength(long number) //text
    {
        return attr("maxlength",number);
    }
    public FormControlInputText pattern(String regex) 
    {
        return attr("pattern",regex);
    }
    public FormControlInputText placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    public FormControlInputText list(String id)
    {
        return attr("list",id);
    }
    
    public FormControlInputText size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public FormControlInputText required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public FormControlInputText value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }

}

