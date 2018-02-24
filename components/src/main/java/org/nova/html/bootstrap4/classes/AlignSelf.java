package org.nova.html.bootstrap4.classes;

public enum AlignSelf
{
    start("align-self-start"), 
    center("align-self-center"), 
    end("align-self-end"), 
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
