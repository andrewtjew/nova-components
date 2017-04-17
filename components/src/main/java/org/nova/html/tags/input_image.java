package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.enctype;
import org.nova.html.enums.target;;

public class input_image extends InputElement<input_image>
{
    public input_image()
    {
        super();
        attr("type","image");
    }

    public input_image alt(String text) //image
    {
        return attr("alt",text);
    }
    public input_image formenctype(enctype enctype) //submit, image
    {
        return attr("formenctype",enctype.toString());
    }
    public input_image formmethod(String value) //submit, image
    {
        return attr("formmethod",value);
    }
    public input_image formtarget(target target) //submit,image
    {
        return attr("target",target.toString());
    }
    public input_image formtarget(String framename)  //submit,image
    {
        return attr("target",framename);
    }
    public input_image height(String pixels) //image
    {
        return attr("height",pixels);
    }
}
