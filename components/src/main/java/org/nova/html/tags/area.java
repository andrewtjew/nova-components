package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.area_rel;
import org.nova.html.enums.area_shape;
import org.nova.html.enums.target;

public class area extends GlobalEventTagElement<area>
{
    public area()
    {
        super("area",true);
    }
    
    public area alt(String text)
    {
        return attr("alt",text);
    }

    public area coords(String coordinates)
    {
        return attr("coords",coordinates);
    }

    public area download(String filename)
    {
        return attr("download",filename);
    }
    public area href(String URL)
    {
        return attr("href",URL);
    }
    public area hreflang(String language_code)
    {
        return attr("hreflang",language_code);
    }
    public area media(String media_query)
    {
        return attr("media",media_query);
    }
    public area rel(area_rel rel)
    {
        return attr("rel",rel.toString());
    }
    public area shape(area_shape shape)
    {
        return attr("shape",shape.toString());
    }
    public area target(target target)
    {
        return attr("target",target.toString());
    }
    public area target(String framename)
    {
        return attr("target",framename);
    }
    public area type(String media_type)
    {
        return attr("type",media_type);
    }
    
}
