package org.nova.html.bootstrap4;

import org.nova.html.elements.TagElement;

public class CustomLabel extends StyleComponent<CustomLabel>
{
    public CustomLabel()
    {
        super("label","custom-control-label");
    }
    public CustomLabel for_(String element_id)
    {
        return attr("for",element_id);
    }
    public CustomLabel for_(TagElement<?> element)
    {
        return attr("for",element.id());
    }
    public CustomLabel form(String form_id)
    {
        return attr("form",form_id);
    }
}
