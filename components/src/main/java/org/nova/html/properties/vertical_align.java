package org.nova.html.properties;

public enum vertical_align
{
    baseline("baseline"),
    sub("sub"),
    super_("super"),
    top("top"),
    text_top("text-top"),
    middle("middle"),
    bottom("bottom"),
    text_bottom("text-bottom"),
    initial("initial"),
    inherit("inherit")
    ;
    final String value;
    vertical_align(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
