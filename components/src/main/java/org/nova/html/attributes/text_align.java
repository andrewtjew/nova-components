package org.nova.html.attributes;

public enum text_align
{
    left("left"),
    right("right"),
    center("center"),
    justify("justify"),
    initial("initial"),
    inherit("inherit")
    ;
    final String value;
    text_align(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
