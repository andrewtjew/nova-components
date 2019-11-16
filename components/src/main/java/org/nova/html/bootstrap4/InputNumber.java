package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;

public class InputNumber extends InputComponent<InputNumber>
{
    public InputNumber()
    {
        super("input","number");
    }
    public InputNumber max(double number)
    {
        return attr("max",number);
    }
    public InputNumber max(long number)
    {
        return attr("max",number);
    }
    public InputNumber min(double number)
    {
        return attr("min",number);
    }
    public InputNumber min(long number)
    {
        return attr("min",number);
    }
    public InputNumber step(double number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }
    public InputNumber step(long number) //number, range, date, datetime, datetime-local, month, time and week.
    {
        return attr("step",number);
    }

    public InputNumber required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public InputNumber required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    public InputNumber value(Double value)
    {
        return attr("value",value);
    }
    public InputNumber value(double value)
    {
        return attr("value",value);
    }
    public InputNumber value(Long value)
    {
        return attr("value",value);
    }
    public InputNumber value(long value)
    {
        return attr("value",value);
    }
    public InputNumber value(Integer value)
    {
        return attr("value",value);
    }
    public InputNumber value(int value)
    {
        return attr("value",value);
    }
    public InputNumber size(int number) //text, search, tel, url, email, and password.
    {
        return attr("size",number);
    }
    public InputNumber placeholder(String text) //text, search, url, tel, email, and password.
    {
        return attr("placeholder",text);
    }
    public InputNumber autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public InputNumber autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
}

