package org.nova.html.bootstrap4.classes;

public enum JustifyContent
{
    start("justify_content-start"), 
    center("justify_content-center"), 
    end("justify_content-end"), 
    around("justify_content-around"), 
    between("justify_content-between"), 
    ;
    private String value;

    JustifyContent(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
