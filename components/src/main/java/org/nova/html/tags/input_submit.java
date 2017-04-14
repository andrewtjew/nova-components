package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.enctype;

public class input_submit extends InputElement<input_submit>
{
    public input_submit()
    {
        super();
        attr("type","submit");
    }
    public input_submit formaction(String URL) //submit, image
    {
        return attr("formaction",URL);
    }
    public input_submit formenctype(enctype enctype) //submit, image
    {
        return attr("formenctype",enctype);
    }
    public input_submit formmethod(String value) //submit, image
    {
        return attr("formmethod",value);
    }
    public input_submit formnovalidate()  //submit
    {
        return attr("formnovalidate");
    }
    public input_submit formnovalidate(boolean formnovalidate)  //submit
    {
        if (formnovalidate)
        {
            return attr("formnovalidate");
        }
        return this;
    }
    public input_submit value(String text) //button, reset, submit, text, password, hidden, checkbox, radio, image
    {
        return attr("value",text);
    }
    
}
