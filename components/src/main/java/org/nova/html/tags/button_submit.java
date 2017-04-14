package org.nova.html.tags;

import org.nova.html.elements.ButtonElement;
import org.nova.html.enums.target;

public class button_submit extends ButtonElement<button_submit>
{
    public button_submit()
    {
        super();
        attr("type","submit");
    }
    public button_submit formaction(String URL) 
    {
        return attr("formaction",URL);
    }
    public button_submit formenctype(String value)
    {
        return attr("formenctype",value);
    }
    public button_submit formmethod(String value) 
    {
        return attr("formmethod",value);
    }
    public button_submit formnovalidate()  //submit
    {
        return attr("formnovalidate");
    }
    public button_submit formnovalidate(boolean formnovalidate)
    {
        if (formnovalidate)
        {
            attr("formnovalidate");
        }
        return this;
    }
    public button_submit formtarget(target target)
    {
        return attr("target",target);
    }
    public button_submit formtarget(String framename)
    {
        return attr("target",framename);
    }
}
