package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_radio;

public class FormControlInputCheckRadio extends FormControlInputComponent<FormControlInputCheckRadio>
{
    public FormControlInputCheckRadio()
    {
        super("radio");
    }
    public FormControlInputCheckRadio checked() //checkbox or radio
    {
        return attr("checked");
    }
    public FormControlInputCheckRadio checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public FormControlInputCheckRadio value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    public FormControlInputCheckRadio value(Object value)
    {
        if (value==null)
        {
            return this;
        }
        return attr("value",value.toString());
    }
}

