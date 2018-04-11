package org.nova.html.attributes;

public enum text_decoration
{
    none("none"),
    underline("underline"),
    overline("overline"),
    line_through("line-through"),
    initial("initial"),
    inherit("inherit")
    ;
    final String value;
    text_decoration(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
