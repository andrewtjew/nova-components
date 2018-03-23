package org.nova.html.widgets;

import org.nova.html.tags.button_button;

public class remove_button extends button_button
{
    public remove_button()
    {
        style("cursor:pointer;float:right;");
        addInner("&#10060;");
        
    }
}
