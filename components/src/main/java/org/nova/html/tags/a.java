package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class a extends GlobalEventTagElement<a>
{
    public a()
    {
        super("a");
    }
    
    public a href(String URL)
    {
        return attr("href",URL);
    }
    public a download(String filename)
    {
        return attr("download",filename);
    }
    public a hreflang(String language_code)
    {
        return attr("hreflang",language_code);
    }
    public a media(String media_query)
    {
        return attr("media",media_query);
    }
    public a rel(rel rel)
    {
        return attr("rel",rel.toString());
    }
    public a target(target target)
    {
        return attr("target",target.toString());
    }
    public a target(String framename)
    {
        return attr("target",framename);
    }
    public a type(String media_type)
    {
        return attr("type",media_type);
    }
}
