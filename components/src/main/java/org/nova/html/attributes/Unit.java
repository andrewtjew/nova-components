package org.nova.html.attributes;

public enum Unit
{
    em("em"),
    ex("ex"),
    ch("ch"),
    rem("rem"),
    vw("vw"),
    vh("vh"),
    vmin("vmin"),
    vmax("vmax"),
    percent("%"),
    
    ;
    private String value;
    Unit(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }    
}
