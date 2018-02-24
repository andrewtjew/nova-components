package org.nova.html.xtags;

import org.nova.html.properties.Style;
import org.nova.html.properties.text_align;
import org.nova.html.tags.h3;

public class center_h3 extends h3
{
    public center_h3()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
