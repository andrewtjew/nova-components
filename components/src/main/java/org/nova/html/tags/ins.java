
package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.crossorigin;
import org.nova.html.enums.area_shape;
import org.nova.html.enums.sandbox;
import org.nova.html.enums.target;

public class ins extends GlobalEventTagElement<ins>
{
    public ins()
    {
        super("ins");
    }
    
    public ins cite(String URL)
    {
        return attr("cite",URL);
    }
    public ins datetime(String datetime)
    {
        return attr("datetime",datetime);
    }
    public ins usemap(String mapname)
    {
        return attr("usemap",mapname);
    }
    public ins width(int width)
    {
        return attr("width",width);
    }
        
    
}
