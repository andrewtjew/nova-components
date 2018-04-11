package org.nova.html.attributes;

public enum unit
{

    em("em"),
    ex("ex"),
    percent("%"),
    px("px"),
    cm("cm"),
    mm("mm"),
    in("in"),
    pt("pt"),
    pc("pc"),

    ch("ch"),
    rem("rem"),
    vw("vw"),
    vh("vh"),
    vmin("vmin"),
    vmax("vmax"),
    ;

    private String value;
    unit(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }    
    
}
