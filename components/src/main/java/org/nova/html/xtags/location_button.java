package org.nova.html.xtags;

import org.nova.html.properties.Style;
import org.nova.html.properties.text_align;
import org.nova.html.tags.button_button;
import org.nova.html.tags.h1;
import org.nova.html.tags.h3;

public class location_button extends button_button
{
    public location_button(String location)
    {
        onclick("window.location='"+location+"'");
    }
    
}
