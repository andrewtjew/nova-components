package org.nova.html.bootstrap4;

import org.nova.html.enums.autocomplete;
import org.nova.html.tags.input_email;

public class FormControlInputComponent<ELEMENT extends InputComponent<ELEMENT>> extends InputComponent<ELEMENT>
{
    public FormControlInputComponent(String type)
    {
        super("input",type);
        addClass("form-control");
    }
}

