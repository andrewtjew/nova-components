package org.nova.html.enums;

public enum character_set
{
    UTF_8("UTF-8"),
    ;
    private String value;
    character_set(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }
}
