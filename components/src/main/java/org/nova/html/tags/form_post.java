package org.nova.html.tags;

import org.nova.html.enums.method;
import org.nova.html.enums.enctype;
import org.nova.html.elements.FormElement;

public class form_post extends FormElement<form_post>
{
    public form_post()
    {
        super();
        attr("form_post",method.get);    
    }
    public form_post enctype(enctype enctype) 
    {
        return attr("enctype",enctype);
    }

}
