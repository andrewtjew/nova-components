package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.crossorigin;
import org.nova.html.enums.link_rel;

public class link extends GlobalEventTagElement<link>
{
    public link()
    {
        super("link",true);
    }
    
    public link integrity(String code)
    {
        return attr("integrity",code);
    }
    public link crossorigin(crossorigin crossorigin)
    {
        return attr("crossorigin",crossorigin.toString());
    }
    public link href(String URL)
    {
        return attr("href",URL);
    }
    public link hreflang(String language_code)
    {
        return attr("hreflang",language_code);
    }
    public link media(String media_query)
    {
        return attr("media",media_query);
    }
    public link type(String media_type)
    {
        return attr("type",media_type);
    }
    public link rel(link_rel rel)
    {
        return attr("rel",rel);
    }
}
