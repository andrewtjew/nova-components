package org.nova.html.tags;

import org.nova.html.attributes.input_type;
import org.nova.html.elements.InputElement;

public class input extends InputElement<input>
{
    public input()
    {
        super();
        attr("type","email");
    }
    public input type(input_type type)
    {
        attr("type",type);
        return this;
    }
}
