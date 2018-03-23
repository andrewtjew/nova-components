package org.nova.html.tags;

import org.nova.html.elements.InputElement;

public class input_checkbox extends InputElement<input_checkbox>
{
    public input_checkbox()
    {
        super();
        attr("type","checkbox");
    }
    public input_checkbox checked() //checkbox or radio
    {
        return attr("checked");
    }
    public input_checkbox checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public input_checkbox required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_checkbox required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public input_checkbox value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    
}
