package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_hidden extends InputElement<input_hidden>
{
    public input_hidden()
    {
        super();
        attr("type","hidden");
    }

    public input_hidden value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }

    public input_hidden value(Object object) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",object);
    }
    
}
