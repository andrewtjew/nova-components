package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.Align;
import org.nova.html.bootstrap4.classes.AlignItems;
import org.nova.html.bootstrap4.classes.AlignSelf;
import org.nova.html.bootstrap4.classes.DeviceClass;
import org.nova.html.bootstrap4.classes.Display;
import org.nova.html.bootstrap4.classes.Edge;
import org.nova.html.bootstrap4.classes.Flex;
import org.nova.html.bootstrap4.classes.Float_;
import org.nova.html.bootstrap4.classes.Font;
import org.nova.html.bootstrap4.classes.Justify;
import org.nova.html.bootstrap4.classes.Placement;
import org.nova.html.bootstrap4.classes.Position;
import org.nova.html.bootstrap4.classes.Rounded;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.bootstrap4.classes.TextAlign;
import org.nova.html.bootstrap4.classes.TextStyle;
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

