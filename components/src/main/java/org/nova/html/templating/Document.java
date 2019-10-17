package org.nova.html.templating;

import java.util.HashMap;

import org.nova.html.deprecated.Content;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;

public class Document extends Element
{
    final HashMap<String,Content> map;
    final Content content;
    
    public Document(Template template)
    {
        this.content=new Content();
        this.map=new HashMap<>();
        for (Element element:template.elements)
        {
            if (element instanceof InsertMarker)
            {
                InsertMarker marker=(InsertMarker)element;
                Content markerContent=new Content();
                this.map.put(marker.name,markerContent);
                this.content.addInner(markerContent);
            }
            else
            {
                this.content.addInner(element);
            }
        }
    }
    
    public <E extends Element> E fill(String name,E element)
    {
        if (element!=null)
        {
            this.map.get(name).addInner(element);
        }
        return element;
    }

    public Object fill(String name,Object object)
    {
        if (object!=null)
        {
            this.map.get(name).addInner(object);
        }
        return object;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.content);
    }

}
