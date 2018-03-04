package org.nova.html.bootstrap4.classes;

public enum ButtonStyle
{
    primary("primary"), 
    secondary("secondary"), 
    success("success"), 
    info("info"), 
    warninig("warninig"), 
    danger("danger"), 
    dark("dark"), 
    light("light"), 
        ;
    private String value;

    ButtonStyle(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
