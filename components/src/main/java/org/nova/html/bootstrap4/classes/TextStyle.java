package org.nova.html.bootstrap4.classes;

public enum TextStyle
{
    justify("justify"), 
    monopace("monospace"), 
    nowrap("nowrap"), 
    lowercase("lowercase"), 
    uppercase("uppercase"), 
    capitalize("capitalize"), 
    ;
    
    private String value;

    TextStyle(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
