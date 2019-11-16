package org.nova.html.bootstrap4;

import org.nova.html.elements.Element;
import org.nova.html.elements.TagElement;

public class Collapse extends StyleComponent<Collapse>
{
    public Collapse()
    {
        super("div","collapse");
    }
    
    public Collapse show()
    {
        addClass("show");
        return this;
    }

    public Collapse linkDataParent(String parent)
    {
        attr("data-parent",parent);
        return this;
    }

    public Collapse linkDataParent(TagElement<?> parent)
    {
        attr("data-parent","#"+parent.id());
        return this;
    }

}
