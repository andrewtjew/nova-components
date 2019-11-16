package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_radio;

public class InputCheckbox extends InputComponent<InputCheckbox>
{
    public InputCheckbox()
    {
        super("input","checkbox");
    }
    public InputCheckbox checked() //checkbox or radio
    {
        return attr("checked");
    }
    public InputCheckbox checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public InputCheckbox value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    public InputCheckbox value(Object value)
    {
        if (value==null)
        {
            return this;
        }
        return attr("value",value.toString());
    }
}

