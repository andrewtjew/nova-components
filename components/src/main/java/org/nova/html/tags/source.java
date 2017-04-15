
package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.crossorigin;
import org.nova.html.enums.area_shape;
import org.nova.html.enums.sandbox;
import org.nova.html.enums.target;

public class source extends GlobalEventTagElement<source>
{
    public source()
    {
        super("source");
    }
    
    public source src(String URL)
    {
        return attr("src",URL);
    }
    public source media(String media_query)
    {
        return attr("media",media_query);
    }
    public source sizes(String sizes)
    {
        return attr("sizes",sizes);
    }
    public source type(String MIME_type)
    {
        return attr("type",MIME_type);
    }
        
    
}
