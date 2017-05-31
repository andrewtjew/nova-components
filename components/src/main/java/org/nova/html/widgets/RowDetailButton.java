package org.nova.html.widgets;

import org.nova.html.tags.button_button;

public class RowDetailButton extends button_button
{
    public RowDetailButton(String location)
    {
        this();  
        onclick("window.location='"+location+"'");
    }
    public RowDetailButton()
    {
        addInner("&#9654;");  
        style("cursor:pointer;float:right;");
    }
}
