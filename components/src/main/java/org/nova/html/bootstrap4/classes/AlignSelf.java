package org.nova.html.bootstrap4.classes;

public enum AlignSelf
{
    start("start"), 
    center("center"), 
    end("end"),
    baseline("baseline"),
    stretch("stretch"),
    ;
    private String value;

    AlignSelf(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
