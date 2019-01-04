package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_checkbox;

public class FormCheckBox extends InputComponent<FormCheckBox>
{
    public FormCheckBox()
    {
        super("form-check-input","checkbox");
        
    }
    public FormCheckBox checked() //checkbox or radio
    {
        return attr("checked");
    }
    public FormCheckBox checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public FormCheckBox required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public FormCheckBox required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public FormCheckBox value(Object text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
}

