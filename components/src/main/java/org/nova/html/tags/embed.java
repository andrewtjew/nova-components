package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.area_rel;
import org.nova.html.enums.area_shape;
import org.nova.html.enums.target;

public class embed extends GlobalEventTagElement<embed>
{
    public embed()
    {
        super("embed");
    }
    
    public embed height(int height)
    {
        return attr("height",height);
    }

    public embed src(String URL)
    {
        return attr("src",URL);
    }
    public embed type(String media_type)
    {
        return attr("type",media_type);
    }
    public embed width(int width)
    {
        return attr("width",width);
    }
    
}
