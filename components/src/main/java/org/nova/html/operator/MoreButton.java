package org.nova.html.operator;

import org.nova.html.deprecated.LinkButton;
import org.nova.html.ext.Head;

public class MoreButton extends LinkButton
{
    public MoreButton(Head head,String href)
    {
        super(head,href,"&#8230;","More...");
    }
}
