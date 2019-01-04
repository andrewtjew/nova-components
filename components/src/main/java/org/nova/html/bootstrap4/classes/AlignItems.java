package org.nova.html.bootstrap4.classes;

public enum AlignItems
{
    start("start"), 
    center("center"), 
    end("end"), 
    baseline("baseline"), 
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
