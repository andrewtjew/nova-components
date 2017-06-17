package org.nova.html.properties;

public enum font_weight
{
    normal("normal"),
    bold("bold"),
    bolder("bolder"),
    lighter("lighter"),
    _100("100"),
    _200("200"),
    _300("300"),
    _400("400"),
    _500("500"),
    _600("600"),
    _700("700"),
    _800("800"),
    _900("900"),
    initial("initial"),
    inherit("inherit")
    ;
    final String value;
    font_weight(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
