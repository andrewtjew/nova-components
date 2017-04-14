package org.nova.html.tags;

import org.nova.html.enums.method;
import org.nova.html.elements.FormElement;
import org.nova.html.elements.GlobalEventTagElement;

public class form_get extends FormElement<form_get>
{
    public form_get()
    {
        super();
        attr("method",method.get);    
    }
}
