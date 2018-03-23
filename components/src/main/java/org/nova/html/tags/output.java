package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class output extends GlobalEventTagElement<output>
{
    public output()
    {
        super("label");
    }
    
    public output for_(String element_id)
    {
        return attr("for",element_id);
    }
    public output form(String form_id)
    {
        return attr("form",form_id);
    }
    public output name(String name)
    {
        return attr("name",name);
    }
    
}
