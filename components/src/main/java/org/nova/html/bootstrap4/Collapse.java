package org.nova.html.bootstrap4;

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

}
