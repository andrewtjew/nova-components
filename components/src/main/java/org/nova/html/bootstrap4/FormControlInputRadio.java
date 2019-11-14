package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_radio;

public class FormControlInputRadio extends FormControlInputComponent<FormControlInputRadio>
{
    public FormControlInputRadio()
    {
        super("radio");
    }
    public FormControlInputRadio checked() //checkbox or radio
    {
        return attr("checked");
    }
    public FormControlInputRadio checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public FormControlInputRadio value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    public FormControlInputRadio value(Object value)
    {
        if (value==null)
        {
            return this;
        }
        return attr("value",value.toString());
    }
}

