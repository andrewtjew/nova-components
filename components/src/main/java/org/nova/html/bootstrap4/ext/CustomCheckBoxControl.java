package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.CustomCheckBox;
import org.nova.html.bootstrap4.CustomControl;
import org.nova.html.bootstrap4.CustomLabel;
import org.nova.html.bootstrap4.CustomRadio;
import org.nova.html.bootstrap4.StyleComponent;

public class CustomCheckBoxControl extends StyleComponent<CustomControl>
{
    private CustomCheckBox customCheckBox;
    private CustomLabel customLabel;

    public CustomCheckBoxControl(String label,String name,Object value)
    {
        super("div", "custom-control");
        addClass("custom-checkbox");
        this.customCheckBox = returnAddInner(new CustomCheckBox()).name(name);
        if (value!=null)
        {
            this.customCheckBox.value(value);
        }
        this.customCheckBox.id();
        this.customLabel=returnAddInner(new CustomLabel()).for_(customCheckBox);
        if (label!=null)
        {
            this.customLabel.addInner(label);
        }
        else
        {
            this.customLabel.addInner("&#x200B;");
        }
    }

    public CustomCheckBoxControl(String label,String name,String id,Object value)
    {
        super("div", "custom-control");
        addClass("custom-checkbox");
        this.customCheckBox = returnAddInner(new CustomCheckBox()).name(name);
        if (value!=null)
        {
            this.customCheckBox.value(value);
        }
        if (id!=null)
        {
            this.customCheckBox.id(id);
        }
        else
        {
            this.customCheckBox.id();
        }
        this.customLabel=returnAddInner(new CustomLabel()).for_(customCheckBox);
        if (label!=null)
        {
            this.customLabel.addInner(label);
        }
        else
        {
            this.customLabel.addInner("&#x200B;");
        }
    }
    
    public CustomCheckBoxControl(String label,String name)
    {
        this(label,name,null);
    }
    
    public CustomCheckBox checkBox()
    {
        return this.customCheckBox;
    }
}
