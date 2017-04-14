package org.nova.html.elements;

import org.nova.html.enums.enctype;
import org.nova.html.enums.autocomplete;
import org.nova.html.enums.target;

import com.nimbusds.oauth2.sdk.http.HTTPRequest.Method;

public class FormElement<ELEMENT extends FormElement<ELEMENT>> extends GlobalEventTagElement<ELEMENT>
{
    public FormElement()
    {
        super("form");
    }
    
    public  ELEMENT action(String URL) 
    {
        return attr("action",URL);
    }
    public ELEMENT autocomplete(autocomplete autocomplete) 
    {
        return attr("autocomplete",autocomplete);
    }
    public ELEMENT novalidate()  
    {
        return attr("novalidate");
    }
    public ELEMENT formnovalidate(boolean novalidate)  
    {
        if (novalidate)
        {
            return attr("novalidate");
        }
        return (ELEMENT)this;
    }
    public ELEMENT formtarget(target target)
    {
        return attr("target",target);
    }
    public ELEMENT formtarget(String framename)
    {
        return attr("target",framename);
    }
}
