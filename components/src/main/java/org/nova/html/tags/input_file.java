package org.nova.html.tags;

import org.nova.html.elements.InputElement;
import org.nova.html.enums.autocomplete;

public class input_file extends InputElement<input_file>
{
    public input_file()
    {
        super();
        attr("type","file");
    }
    public input_file accept(String value) //file
    {
        return attr("accept",value);
    }
    public input_file multiple() //file
    {
        return attr("multiple");
    }
    public input_file multiple(boolean multiple) //file, email
    {
        if (multiple)
        {
            attr("multiple");
        }
        return this;
    }
    public input_file required()  //text, search, url, tel, email, password, date pickers, number, checkbox, radio, and file.
    {
        return attr("required");
    }
    public input_file required(boolean required)
    {
        if (required)
        {
            attr("required");
        }
        return this;
    }
    
}
