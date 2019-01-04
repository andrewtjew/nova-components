package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.Size;

public abstract class ButtonComponent<ELEMENT extends ButtonComponent<ELEMENT>> extends ToggleComponent<ELEMENT> 
{
    public ButtonComponent(String tag)
    {
        super(tag,"btn");
    }

    @SuppressWarnings("unchecked")
    public ELEMENT size(Size value)
    {
        addClass("btn",value);
        return (ELEMENT)this;
    }
    
    public ELEMENT name(String name)
    {
        attr("name",name);
        return (ELEMENT)this;
    }
    
}
