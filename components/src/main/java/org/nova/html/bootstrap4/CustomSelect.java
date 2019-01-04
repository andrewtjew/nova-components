package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.DeviceClass;

public class CustomSelect extends InputComponent<CustomSelect>
{
    public CustomSelect()
    {
        super("select","custom-select",null);
    }
    public CustomSelect(DeviceClass deviceClass)
    {
        this();
        addClass("custom-select-"+deviceClass);
    }
    public CustomSelect multiple()
    {
        return attr("multiple");
    }
    public CustomSelect multiple(boolean multiple)
    {
        if (multiple)
        {
            return attr("multiple");
        }
        return this;
    }
    public CustomSelect required()
    {
        return attr("required");
    }
    public CustomSelect required(boolean required)
    {
        if (required)
        {
            return attr("required");
        }
        return this;
    }
    public CustomSelect size(int number)
    {
        return attr("size",number);
    }
}
