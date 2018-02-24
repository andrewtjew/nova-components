package org.nova.html.bootstrap4.classes;

public enum AlignItems
{
    start("align-items-start"), 
    center("align-items-center"), 
    end("align-items-end"), 
    ;
    private String value;

    AlignItems(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
