package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Item;
import org.nova.html.bootstrap4.Modal;
import org.nova.html.bootstrap4.ModalBody;
import org.nova.html.bootstrap4.ModalContent;
import org.nova.html.bootstrap4.ModalDialog;
import org.nova.html.bootstrap4.ModalFooter;
import org.nova.html.bootstrap4.ModalHeader;
import org.nova.html.bootstrap4.Spinner;
import org.nova.html.bootstrap4.SpinnerType;
import org.nova.html.bootstrap4.StyleComponent;
import org.nova.html.bootstrap4.classes.DeviceClass;
import org.nova.html.bootstrap4.classes.Justify;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.remoting.ModalOption;

public class SpinnerModal extends Modal
{
    public SpinnerModal(boolean centered,Element message)
    {
        this(centered,message,new Spinner(SpinnerType.border).text(StyleColor.white),null);
    }
    public SpinnerModal(boolean centered,Element message,Spinner spinner,DeviceClass deviceClass)
    {
        ModalDialog dialog=returnAddInner(new ModalDialog());
        
        if (centered)
        {
            dialog.centered();
        }
        if (deviceClass!=null)
        {
            dialog.deviceClass(deviceClass);
        }
        ModalBody body=dialog.returnAddInner(new ModalBody());
        Item item=body.returnAddInner(new Item()).justify_content(Justify.center).d_flex();
        item.addInner(spinner);
        item.addInner(message);
        
    }
    
    
    public String js_option(ModalOption option)
    {
        return "$('#"+id()+"').modal('"+option+"');";
    }
}