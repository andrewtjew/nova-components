package org.nova.html.enums;

public enum type
{
    _1("1"),
    A("A"),
    a("a"),
    I("I"),
    i("i"),
    UTF_8("UTF-8"),
    ;
    private String value;
    type(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }
}
