package org.nova.html.enums;

public enum sorted
{
    reversed("reversed"),
    number("number"),
    reversed_number("reversed number"),
    number_reversed("number reversed"),
    ;
    private String value;
    sorted(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }
}