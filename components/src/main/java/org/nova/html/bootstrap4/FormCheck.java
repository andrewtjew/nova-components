package org.nova.html.bootstrap4;

import org.nova.html.tags.label;
import org.nova.html.tags.small;

public class FormCheck extends Component<FormCheck>
{
    public FormCheck()
    {
        super("div","form-check");
    }
    
    public FormCheck inline()
    {
        this.addClass("form-check-inline");
        return this;
    }

    public FormCheck add(String labelText,FormCheckBox element)
    {
        returnAddInner(element);
        label label=returnAddInner(new label()).addInner(labelText);
        label.for_(element);
        return this;
    }
    
}
