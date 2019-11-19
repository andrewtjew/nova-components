package org.nova.html.bootstrap4.classes;

public enum StyleColor
{
    muted("muted"), 
    primary("primary"), 
    success("success"), 
    info("info"), 
    warning("warning"), 
    danger("danger"), 
    secondary("secondary"), 
//    active("active"), 
    light("light"), 
    dark("dark"), 
    white("white"), 
    transparent("transparent")
    ;
    private String value;

    StyleColor(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
    
}
