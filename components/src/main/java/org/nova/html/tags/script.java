
package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.crossorigin;
import org.nova.html.enums.area_shape;
import org.nova.html.enums.character_set;
import org.nova.html.enums.sandbox;
import org.nova.html.enums.target;

public class script extends GlobalEventTagElement<script>
{
    public script()
    {
        super("script");
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
        return attr("src",URL);
    }
    public script type(String media_type)
    {
        return attr("type",media_type);
    }
         
    
}
