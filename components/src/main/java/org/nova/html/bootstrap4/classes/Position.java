package org.nova.html.bootstrap4.classes;

public enum Position
{
    static_("static"), 
    relative("relative"),
    absolute("absolute"),
    fixed("fixed"),
    stickey("sticky"),
    ;
    
    private String value;

    Position(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
