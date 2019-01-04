package org.nova.html.bootstrap4.classes;

public enum Edge
{
    left("left"), 
    right("right"), 
    bottom("bottom"), 
    top("top"),
    ;
    
    private String value;

    Edge(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
