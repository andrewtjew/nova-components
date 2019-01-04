package org.nova.html.tags.ext;

import org.nova.html.attributes.Style;
import org.nova.html.attributes.text_align;
import org.nova.html.tags.h5;

public class h5_center extends h5
{
    public h5_center()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
