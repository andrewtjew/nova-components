package org.nova.html.bootstrap4.classes;

public enum Justify
{
    start("start"), 
    center("center"), 
    end("end"), 
    around("around"), 
    between("between"), 
    ;
    private String value;

    Justify(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
