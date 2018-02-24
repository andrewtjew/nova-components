package org.nova.html.bootstrap4.classes;

public enum TextColor
{
    muted("text-muted"), 
    primary("text-primary"), 
    success("text-success"), 
    info("text-info"), 
    warninig("text-warninig"), 
    danger("text-danger"), 
    secondary("text-secondary"), 
    active("text-active"), 
    light("text-light"), 
    dark("text-dark"), 
    white("text-white"), 
    ;
    private String value;

    TextColor(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
