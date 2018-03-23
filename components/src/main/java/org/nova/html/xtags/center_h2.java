package org.nova.html.xtags;

import org.nova.html.properties.Style;
import org.nova.html.properties.text_align;
import org.nova.html.tags.h2;

public class center_h2 extends h2
{
    public center_h2()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
