package org.nova.html.bootstrap4.classes;

public enum NavbarColor
{
    light("navbar-light"), 
    dark("navbar-dark"), 
    ;
    private String value;

    NavbarColor(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
