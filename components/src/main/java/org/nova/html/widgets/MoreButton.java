package org.nova.html.widgets;

import org.nova.html.attributes.Style;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.link;

public class MoreButton extends LinkButton
{
    public MoreButton(Head head,String href)
    {
        super(head,href,"&#9654;","More...");
    }
}
