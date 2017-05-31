package org.nova.html.properties;

public enum position
{
    static_("static"),
    relative("relative"),
    fixed("fixed"),
    absolute("absolute"),
    ;
    final String value;
    position(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
