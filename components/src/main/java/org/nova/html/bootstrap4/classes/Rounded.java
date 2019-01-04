package org.nova.html.bootstrap4.classes;

public enum Rounded
{
    top("top"), 
    right("right"), 
    bottom("bottom"), 
    left("left"), 
    circle("circle"), 
    ;
    private String value;

    Rounded(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
