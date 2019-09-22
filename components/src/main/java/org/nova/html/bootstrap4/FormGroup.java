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
    private DeviceClass deviceClass;
    private int labelSize;
    public FormGroup row(DeviceClass deviceClass,int labelSize)
    {
        this.deviceClass=deviceClass;
        this.labelSize=labelSize;
        this.addClass("row");
        return this;
    }
    public FormGroup custom_control()
    {
        addClass("custom-control");
        return this;
    }
    public FormGroup addLabelRow(String labelText,TagElement<?> element)
    {
//        FormLabel label=returnAddInner(new FormLabel(this.deviceClass,this.labelSize)).addInner(labelText).text(TextAlign.right);
        if (this.deviceClass!=null)
        {
            FormLabel label=returnAddInner(new FormLabel(this.deviceClass,this.labelSize)).addInner(labelText).text(TextAlign.right);
            Item item=returnAddInner(new Item()).addInner(element).col(this.deviceClass,12-this.labelSize);
            label.for_(item);
        }
        else
        {
            FormLabel label=returnAddInner(new FormLabel()).addInner(labelText).text(TextAlign.right);
            Item item=returnAddInner(new Item()).addInner(element);
            label.for_(item);
            
        }
        
        return this;
    }

    public FormGroup addLabelColumn(String labelText,FormControlInputComponent<?> element)
    {
        return addLabelColumn(labelText, element,null);
    }
    public FormGroup addLabelColumn(String labelText,FormControlInputComponent<?> element,String help)
    {
        label label=returnAddInner(new label()).addInner(labelText);
        returnAddInner(element);
        label.for_(element);
        returnAddInner(new small().addInner(help));
        return this;
    }
    
}
