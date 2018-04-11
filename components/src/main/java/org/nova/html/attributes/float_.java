package org.nova.html.attributes;

public enum float_
{
    none("none"),
    left("left"),
    right("right"),
    initial("initial"),
    inherit("inherit"),
    ;
    final String value;
    float_(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
