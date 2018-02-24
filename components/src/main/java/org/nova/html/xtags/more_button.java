package org.nova.html.xtags;

import org.nova.html.tags.button_button;

public class more_button extends button_button
{
    public more_button(String location)
    {
        this();  
        onclick("window.location='"+location+"'");
    }
    public more_button()
    {
        addInner("&#9654;");  
        style("cursor:pointer;float:right;");
    }
}
