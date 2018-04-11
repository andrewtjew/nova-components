package org.nova.html.xtags;

import org.nova.html.attributes.Style;
import org.nova.html.attributes.text_align;
import org.nova.html.tags.h2;

public class h2_center extends h2
{
    public h2_center()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
