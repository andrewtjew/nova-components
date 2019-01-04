package org.nova.html.bootstrap4;

public class CustomCheckBox extends InputComponent<CustomCheckBox>
{

    public CustomCheckBox()
    {
        super("custom-control-input","checkbox");
    }
    public CustomCheckBox checked()
    {
        attr("checked");
        return this;
    }
    public CustomCheckBox checked(boolean checked)
    {
        if (checked)
        {
            attr("checked");
        }
        return this;
    }
    public CustomCheckBox required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public CustomCheckBox required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public CustomCheckBox value(Object text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
}
