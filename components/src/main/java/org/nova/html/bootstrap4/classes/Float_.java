package org.nova.html.bootstrap4.classes;

public enum Float_
{
    left("left"), 
    right("right"),
    none("none"),
    ;
    
    private String value;

    Float_(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
