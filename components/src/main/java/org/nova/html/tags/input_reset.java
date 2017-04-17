package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_reset extends InputElement<input_reset>
{
    public input_reset()
    {
        super();
        attr("type","reset");
    }
    
    public input_reset value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    
}
