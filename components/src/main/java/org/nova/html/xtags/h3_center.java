package org.nova.html.xtags;

import org.nova.html.attributes.Style;
import org.nova.html.attributes.text_align;
import org.nova.html.tags.h3;

public class h3_center extends h3
{
    public h3_center()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
