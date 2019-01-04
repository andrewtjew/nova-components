package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.DeviceClass;

public class Col extends StyleComponent<Col>
{
    public Col()
    {
        super("div","col");
    }
    public Col(DeviceClass deviceClass)
    {
        super("div",new ClassBuilder().addClass("col", deviceClass).toString());
    }
    public Col(DeviceClass deviceClass,int columns)
    {
        super("div",new ClassBuilder().addClass("col", deviceClass,columns).toString());
    }
    public Col(int columns)
    {
        super("div",new ClassBuilder().addClass("col", columns).toString());
    }
}
