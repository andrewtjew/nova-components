package org.nova.html.tags;

import org.nova.html.elements.InputElement;

public class input_radio extends InputElement<input_radio>
{
    public input_radio()
    {
        super();
        attr("type","radio");
    }


    public input_radio checked() //checkbox or radio
    {
        return attr("checked");
    }
    public input_radio checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public input_radio value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    public input_radio value(Object value)
    {
        if (value==null)
        {
            return this;
        }
        return attr("value",value.toString());
    }
    
}
