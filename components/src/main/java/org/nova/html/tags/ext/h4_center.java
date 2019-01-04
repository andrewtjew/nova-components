package org.nova.html.tags.ext;

import org.nova.html.attributes.Style;
import org.nova.html.attributes.text_align;
import org.nova.html.tags.h4;

public class h4_center extends h4
{
    public h4_center()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
