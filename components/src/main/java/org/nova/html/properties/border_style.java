package org.nova.html.properties;

public enum border_style
{
    hidden("hidden"),
    dotted("dotted"),
    dashed("dashed"),
    solid("solid"),
    double_("double"),
    groove("groove"),
    ridge("ridge"),
    inset("inset"),
    outset("outset"),
    none("none"),
    initial("initial"),
    inherit("inherit")
    ;
    final String value;
    border_style(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
