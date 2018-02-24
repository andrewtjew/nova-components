package org.nova.html.elements;

import org.nova.html.elements.GlobalEventTagElement;

import org.nova.html.enums.target;
import org.nova.html.enums.crossorigin;
import org.nova.html.enums.link_rel;

public class LinkElement <ELEMENT extends LinkElement<ELEMENT>> extends GlobalEventTagElement<ELEMENT>
{
    public LinkElement()
    {
        super("link");
    }
    
    public ELEMENT crossorigin(crossorigin crossorigin)
    {
        return attr("crossorigin",crossorigin.toString());
    }
    public ELEMENT href(String URL)
    {
        return attr("href",URL);
    }
    public ELEMENT hreflang(String language_code)
    {
        return attr("hreflang",language_code);
    }
    public ELEMENT media(String media_query)
    {
        return attr("media",media_query);
    }
    public ELEMENT type(String media_type)
    {
        return attr("type",media_type);
    }
    public ELEMENT integrity(String code)
    {
        return attr("integrity",code);
    }
    
}
