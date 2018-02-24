package org.nova.html.bootstrap4.classes;

public enum BackgroundColor
{
    primary("bg-primary"), 
    success("bg-success"), 
    info("bg-info"), 
    warninig("bg-warninig"), 
    danger("bg-danger"), 
    secondary("bg-secondary"), 
    light("bg-light"), 
    dark("bg-dark"), 
    ;
    private String value;

    BackgroundColor(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
