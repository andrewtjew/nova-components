package org.nova.html.bootstrap4.classes;

public enum ComponentClass
{
    btn("btn"), 
    ;
    private String value;

    ComponentClass(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
