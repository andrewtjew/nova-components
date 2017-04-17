package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.enctype;

public class input_button extends InputElement<input_button>
{
    public input_button()
    {
        super();
        attr("type","button");
    }
    public input_button value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    
}
