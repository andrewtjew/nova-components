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

    public Collapse linkDataParent(String value)
    {
        attr("data-parent",value);
        return this;
    }

    public Collapse linkDataParent(TagElement<?> element)
    {
        attr("data-parent","#"+element.id());
        return this;
    }

}
