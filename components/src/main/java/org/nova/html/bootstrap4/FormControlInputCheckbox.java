package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_radio;

public class FormControlInputCheckbox extends FormControlInputComponent<FormControlInputCheckbox>
{
    public FormControlInputCheckbox()
    {
        super("checkbox");
    }
    public FormControlInputCheckbox checked() //checkbox or radio
    {
        return attr("checked");
    }
    public FormControlInputCheckbox checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public FormControlInputCheckbox value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    public FormControlInputCheckbox value(Object value)
    {
        if (value==null)
        {
            return this;
        }
        return attr("value",value.toString());
    }
}

