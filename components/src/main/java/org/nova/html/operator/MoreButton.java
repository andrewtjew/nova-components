package org.nova.html.operator;

import org.nova.html.ext.Head;
import org.nova.html.widgets.LinkButton;

public class MoreButton extends LinkButton
{
    public MoreButton(Head head,String href)
    {
        super(head,href,"&#9654;","More...");
    }
}
