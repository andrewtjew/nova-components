package org.nova.html.bootstrap4.classes;

public enum TextAlign
{
    left("left"), 
    center("center"), 
    right("right"), 
    justify("justify"), 
    ;
    
    private String value;

    TextAlign(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
