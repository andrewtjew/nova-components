package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class table extends GlobalEventTagElement<table>
{
    public table()
    {
        super("table");
    }

    /*
    //Not HTML5
    public table border(boolean used)
    {
        return attr("border",used?1:0);
    }
    */

    public table sortable()
    {
        return attr("sortable");
    }
    
}
