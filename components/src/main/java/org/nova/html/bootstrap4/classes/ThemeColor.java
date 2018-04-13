package org.nova.html.bootstrap4.classes;

import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalTagElement;

public enum ThemeColor
{
    muted("muted"), 
    primary("primary"), 
    success("success"), 
    info("info"), 
    warninig("warninig"), 
    danger("danger"), 
    secondary("secondary"), 
    active("active"), 
    light("light"), 
    dark("dark"), 
    white("white"), 
    ;
    private String value;

    ThemeColor(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
    
}
