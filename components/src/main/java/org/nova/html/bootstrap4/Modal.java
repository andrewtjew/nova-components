package org.nova.html.bootstrap4;

public class Modal extends StyleComponent<Modal>
{
    public Modal()
    {
        super("div","modal");
    }
    
    public Modal fade()
    {
        addClass("fade");
        return this;
    }
}
