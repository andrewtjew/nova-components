package org.nova.html.deprecated;

import org.nova.html.tags.button_button;

public class RemoveButton extends button_button
{
    public RemoveButton(String script)
    {
        style("cursor:pointer;float:right;");
        addInner("&#10060;");
        onclick(script);        
    }
}
