package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.DeviceClass;
import org.nova.html.elements.TagElement;

public class FormLabel extends StyleComponent<FormLabel>
{
    public FormLabel()
    {
        super("label","col-form-label");
    }
    public FormLabel(DeviceClass deviceClass,int size)
    {
        super("label","col-form-label");
        col(deviceClass,size);
    }
    public FormLabel for_(String element_id)
    {
        return attr("for",element_id);
    }
    public FormLabel for_(TagElement<?> element)
    {
        return attr("for",element.id());
    }
    public FormLabel form(String form_id)
    {
        return attr("form",form_id);
    }
    
    
}
