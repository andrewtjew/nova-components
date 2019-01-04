package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.DeviceClass;

public class ModalDialog extends StyleComponent<ModalDialog>
{
    public ModalDialog()
    {
        super("div","modal-dialog");
    }
    
    public ModalDialog centered()
    {
        addClass("modal-dialog","centered");
        return this;
    }
    public ModalDialog deviceClass(DeviceClass deviceClass)
    {
        addClass("modal",deviceClass);
        return this;
    }
}
