package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.CustomControl;
import org.nova.html.bootstrap4.CustomLabel;
import org.nova.html.bootstrap4.CustomRadio;
import org.nova.html.bootstrap4.StyleComponent;

public class CustomRadioControl extends StyleComponent<CustomControl>
{
    private CustomRadio customRadio;
    private CustomLabel customLabel;

    public CustomRadioControl(String label,String name,Object value)
    {
        super("div", "custom-control");
        addClass("custom-radio");
        this.customRadio = returnAddInner(new CustomRadio()).name(name).value(value);
        this.customRadio.id();
        this.customLabel=returnAddInner(new CustomLabel()).for_(customRadio);
        if (label!=null)
        {
            this.customLabel.addInner(label);
        }
        else
        {
            this.customLabel.addInner("&#x200B;");
        }
    }
    public CustomRadioControl(String name,Object value,CustomLabel label)
    {
        super("div", "custom-control");
        addClass("custom-radio");
        this.customRadio = returnAddInner(new CustomRadio()).name(name).value(value);
        this.customRadio.id();
        this.customLabel=label;
        this.customLabel.for_(customRadio);
    }
    
    public CustomRadio radio()
    {
        return this.customRadio;
    }
}
