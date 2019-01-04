
package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.crossorigin;

public class img extends GlobalEventTagElement<img>
{
    public img()
    {
        super("img",true);
    }
    
    public img alt(String text)
    {
        return attr("alt",text);
    }

    public img crossorigin(crossorigin crossorigin)
    {
        return attr("crossorigin",crossorigin);
    }

    public img height(int height)
    {
        return attr("height",height);
    }
    public img ismap()
    {
        return attr("ismap");
    }
    public img ismap(boolean ismap)
    {
        if (ismap)
        {
            return attr("ismap");
        }
        return this;
    }
    public img longdesc(String URL)
    {
        return attr("longdesc",URL);
    }
    public img sizes(String sizes)
    {
        return attr("sizes",sizes);
    }
    public img src(String URL)
    {
        return attr("src",URL);
    }
    public img srcset(String HTML_code)
    {
        return attr("srcset",HTML_code);
    }
    public img usemap(String mapname)
    {
        return attr("usemap",mapname);
    }
    public img width(int width)
    {
        return attr("width",width);
    }
        
    
}
