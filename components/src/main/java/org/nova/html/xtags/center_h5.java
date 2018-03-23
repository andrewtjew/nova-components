package org.nova.html.xtags;

import org.nova.html.properties.Style;
import org.nova.html.properties.text_align;
import org.nova.html.tags.h5;

public class center_h5 extends h5
{
    public center_h5()
    {
        style(new Style().text_align(text_align.center));
    }
    
}
