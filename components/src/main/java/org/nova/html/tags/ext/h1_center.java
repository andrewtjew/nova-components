package org.nova.html.tags.ext;

import org.nova.html.attributes.Style;
import org.nova.html.attributes.text_align;
import org.nova.html.tags.h1;

public class h1_center extends h1
{
    public h1_center()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
