package org.nova.html.tags;

import org.nova.html.attributes.input_type;
import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input extends InputElement<input>
{
    public input()
    {
        super();
    }
    public input(datalist datalist)
    {
        super();
        add(datalist);
    }
    public input list(String id)
    {
        attr("list",id);
        return this;
    }
    public input list(datalist datalist)
    {
        list(datalist.id());
        return this;
    }
    public input add(datalist datalist)
    {
        list(datalist.id());
        addInner(datalist);
        return this;
    }
    public input autocomplete(autocomplete autocomplete) //text, search, url, tel, email, password, datepickers, range, and color.
    {
        return attr("autocomplete",autocomplete);
    }
    public input autocomplete(boolean autocomplete)
    {
        if (autocomplete)
        {
            attr("autocomplete");
        }
        return this;
    }
}
