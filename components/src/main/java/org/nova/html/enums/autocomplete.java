package org.nova.html.enums;

public enum autocomplete
{
    on("on"),
    off("off"),
    new_password("new-password"),
    ;
    final String value;
    autocomplete(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
