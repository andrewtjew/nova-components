
package org.nova.html.tags;

import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.crossorigin;
import org.nova.html.enums.character_set;

public class script extends GlobalEventTagElement<script>
{
    public script()
    {
        super("script");
    }
    
    public script integrity(String code)
    {
        return attr("integrity",code);
    }
    public script crossorigin(crossorigin crossorigin)
    {
        return attr("crossorigin",crossorigin.toString());
    }
    public script async()
    {
        return attr("async");
    }
    public script async(boolean async)
    {
        if (async)
        {
            return attr("async");
        }
        return this;
    }
    public script charset(character_set character_set)
    {
        return attr("charset",character_set);
    }
    public script defer()
    {
        return attr("defer");
    }
    public script defer(boolean defer)
    {
        if (defer)
        {
            return attr("defer");
        }
        return this;
    }
    public script src(String URL)
    {
        if (Element.HREF_LOCAL_DIRECTORY!=null)
        {
            URL=URL.replace("http:/", Element.HREF_LOCAL_DIRECTORY);
            URL=URL.replace("https:/", Element.HREF_LOCAL_DIRECTORY);
        }
        return attr("src",URL);
    }
    public script type(String media_type)
    {
        return attr("type",media_type);
    }
         
    
}
