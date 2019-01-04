package org.nova.html.bootstrap4;

public class CustomControl extends StyleComponent<CustomControl>
{

    public CustomControl()
    {
        super("div", "custom-control");
    }
    public CustomControl inline()
    {
        addClass("custom-control-inline");
        return this;
    }
    public CustomControl custom_radio()
    {
        addClass("custom-radio");
        return this;
    }
    public CustomControl custom_checkbox()
    {
        addClass("custom-checkbox");
        return this;
    }

}
