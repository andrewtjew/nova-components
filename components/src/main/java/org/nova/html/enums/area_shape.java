package org.nova.html.enums;

public enum area_shape
{
    default_("default"),
    rect("rect"),
    circle("circle"),
    poly("poly"),
    ;
    private String value;
    area_shape(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }
}
