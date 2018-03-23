package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.bdo_dir;

public class bdo extends GlobalEventTagElement<bdo>
{
    public bdo()
    {
        super("bdo");
    }
    
    public bdo dir(bdo_dir dir)
    {
        return attr("dir",dir.toString());
    }
}
