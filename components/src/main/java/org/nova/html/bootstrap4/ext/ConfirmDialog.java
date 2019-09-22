
package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Button;
import org.nova.html.bootstrap4.ModalCloseButton;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.tags.h5;

public class ConfirmDialog extends ModalDocument 
{ 
    public ConfirmDialog(boolean center,String title,String message,String cancelButtonText,String confirmButtonText,String confirmButtonSCript)
    {
        super(center);
        header().bg(StyleColor.primary).text(StyleColor.light).addInner(new h5().addInner((title)));
        body().addInner(message);
        footer().addInner(new Button().color(StyleColor.success).addInner(confirmButtonText).onclick(confirmButtonSCript));
        footer().addInner(new ModalCloseButton(cancelButtonText));
    }
    public ConfirmDialog(boolean center,String title,String message,String confirmButtonSCript)
    {
        this(center,title,message,"Cancel","OK",confirmButtonSCript);
    }
}
