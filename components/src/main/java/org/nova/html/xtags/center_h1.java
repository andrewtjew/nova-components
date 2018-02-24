package org.nova.html.xtags;

import org.nova.html.properties.Style;
import org.nova.html.properties.text_align;
import org.nova.html.tags.h1;
import org.nova.html.tags.h3;

public class center_h1 extends h1
{
    public center_h1()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
