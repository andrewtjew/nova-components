package org.nova.html.bootstrap4.classes;

public enum Fixed
{
    top("top"), 
    bottom("bottom"), 
    ;
    private String value;

    Fixed(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
