package org.nova.html.widgets;

import org.nova.html.tags.button_button;

public class RowRemoveButton extends button_button
{
    public RowRemoveButton()
    {
        style("cursor:pointer;float:right;");
        addInner("&#10060;");
        
    }
}
