package org.nova.html.xtags;

import org.nova.html.properties.Style;
import org.nova.html.properties.text_align;
import org.nova.html.tags.h3;
import org.nova.html.tags.h6;

public class center_h6 extends h6
{
    public center_h6()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
