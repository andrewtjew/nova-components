package org.nova.html.bootstrap4;

public class InputRadio extends InputComponent<InputRadio>
{
    public InputRadio()
    {
        super("input","radio");
    }
    public InputRadio checked() //checkbox or radio
    {
        return attr("checked");
    }
    public InputRadio checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public InputRadio value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    public InputRadio value(Object value)
    {
        if (value==null)
        {
            return this;
        }
        return attr("value",value.toString());
    }
}

