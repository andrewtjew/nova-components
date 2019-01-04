package org.nova.html.bootstrap4;

import org.nova.html.elements.Element;
import org.nova.html.tags.td;

public class TableRow extends StyleComponent<TableRow>
{
    public TableRow()
    {
        super("tr","table");
    }
    
    public TableRow add(Object...objects)
    {
        if (objects==null)
        {
            addInner(new td());
        }
        else
        {
            for (Object object:objects)
            {
                if (object==null)
                {
                    addInner(new td());
                }
                else if (object instanceof td)
                {
                    addInner(object);
                }
                else if (object instanceof Element)
                {
                    addInner(new td().addInner((Element)object));
                }
                else
                {
                    addInner(new td().addInner(object.toString()));
                }
            }
        }
        return this;
        
    }
}
