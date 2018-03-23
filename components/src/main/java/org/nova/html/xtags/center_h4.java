package org.nova.html.xtags;

import org.nova.html.properties.Style;
import org.nova.html.properties.text_align;
import org.nova.html.tags.h4;

public class center_h4 extends h4
{
    public center_h4()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
