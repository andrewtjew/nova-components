package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.DeviceClass;
import org.nova.html.bootstrap4.classes.TextAlign;
import org.nova.html.elements.TagElement;
import org.nova.html.tags.label;
import org.nova.html.tags.small;

public class FormGroup extends Component<FormGroup>
{
    public FormGroup()
    {
        super("div","form-group");
    }

    public FormGroup row()
    {
        this.addClass("row");
        return this;
    }
    public FormGroup custom_control()
    {
        addClass("custom-control");
        return this;
    }
    
}
