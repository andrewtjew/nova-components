
package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.crossorigin;
import org.nova.html.enums.area_shape;
import org.nova.html.enums.sandbox;
import org.nova.html.enums.target;

public class object extends GlobalEventTagElement<object>
{
    public object()
    {
        super("object");
    }
    
    public object data(String URL)
    {
        return attr("data",URL);
    }

    public object form(String form_id)
    {
        return attr("form",form_id);
    }
    
    public object height(int height)
    {
        return attr("height",height);
    }
    
    public object name(String name)
    {
        return attr("name",name);
    }
    public object type(String media_type)
    {
        return attr("type",media_type);
    }
    public object usemap(String mapname)
    {
        return attr("usemap",mapname);
    }
    public object width(int width)
    {
        return attr("width",width);
    }
        
    
}
