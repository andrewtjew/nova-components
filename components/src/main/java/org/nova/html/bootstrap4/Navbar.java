package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.DeviceClass;
import org.nova.html.bootstrap4.classes.Fixed;

public class Navbar extends StyleComponent<Navbar>
{
    public Navbar()
    {
        super("nav","navbar");
    }
    
    public Navbar fixed(Fixed value)
    {
        addClass("fixed",value);
        return this;
    }
    
    public Navbar expand(DeviceClass deviceClass)
    {
        addClass("navbar-expand",deviceClass);
        return this;
    }
    
}
