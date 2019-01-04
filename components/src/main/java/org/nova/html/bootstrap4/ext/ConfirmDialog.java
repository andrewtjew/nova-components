package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Button;
import org.nova.html.bootstrap4.ModalCloseButton;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.tags.h5;

public class ConfirmDialog extends ModalDocument 
{ 
    public ConfirmDialog(String id,boolean center,String title,String body_id,String message,String cancelButtonText,String confirmButtonText,String confirmButtonSCript)
    {
        super(center);
        id(id);
        header().bg(StyleColor.primary).text(StyleColor.light).addInner(new h5().addInner((title)));
        body().id(body_id);
        body().addInner(message);
        footer().addInner(new Button().color(StyleColor.success).addInner(confirmButtonText).onclick(confirmButtonSCript));
        footer().addInner(new ModalCloseButton(cancelButtonText));
    }
    public ConfirmDialog(String id,boolean center,String title,String body_id,String message,String confirmButtonSCript)
    {
        this(id,center,title,body_id,message,"Cancel","OK",confirmButtonSCript);
    }
    public ConfirmDialog(String id,String title,String message,String confirmButtonSCript)
    {
        this(id,true,title,null,message,confirmButtonSCript);
    }
    public ConfirmDialog(String title,String message,String confirmButtonSCript)
    {
        this(null,title,message,confirmButtonSCript);
    }
}