
package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.ButtonState;
import org.nova.html.elements.TagElement;

public class SubmitButton extends ButtonComponent<SubmitButton>
{
    private ButtonState buttonState;
    private boolean block;
    
    public SubmitButton(String label)
    {
        super("input");
        attr("type","submit");
        attr("value",label);
        
    }
    
    public SubmitButton buttonState(ButtonState value)
    {
        addClass(value);
        return this;
    }
    
    public SubmitButton form(String form_id)
    {
        attr("form",form_id);
        return this;
    }
    public SubmitButton form(TagElement<?> element)
    {
        attr("form",element.id());
        return this;
    }
}
