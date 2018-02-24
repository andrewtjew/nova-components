package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;
import org.nova.html.properties.input_type;

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
