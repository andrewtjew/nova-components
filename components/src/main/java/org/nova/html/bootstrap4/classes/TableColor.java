package org.nova.html.bootstrap4.classes;

public enum TableColor
{
    primary("table-primary"), 
    success("table-success"), 
    danger("table-danger"), 
    info("table-info"), 
    warninig("table-warninig"), 
    active("table-active"), 
    secondary("table-secondary"), 
    light("table-light"), 
    dark("table-dark"), 
    ;
    private String value;

    TableColor(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
