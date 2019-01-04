package org.nova.html.bootstrap4.classes;

public enum Align
{
    start("top"), 
    middle("middle"), 
    bottom("bottom"), 
    text_top("text-top"), 
    text_bottom("text-bottom"), 
    baseline("baseline"), 
    ;
    private String value;

    Align(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
