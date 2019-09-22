package org.nova.html.bootstrap4;

import org.nova.html.tags.input_radio;

public class CustomRadio extends InputComponent<CustomRadio>
{
    public CustomRadio()
    {
        super("custom-control-input","radio");
    }
    public CustomRadio checked() //checkbox or radio
    {
        return attr("checked");
    }
    public CustomRadio checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public CustomRadio value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    public CustomRadio value(Object value)
    {
        if (value==null)
        {
            return this;
        }
        return attr("value",value.toString());
    }
    
}
