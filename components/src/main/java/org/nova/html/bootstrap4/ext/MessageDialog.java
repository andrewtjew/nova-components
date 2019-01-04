package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.ModalCloseButton;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.tags.h5;

public class MessageDialog extends ModalDocument
{
    public MessageDialog(String id,boolean center,String title,String body_id,String message,String closeButtonText)
    {
        super(center);
        id(id);
        header().bg(StyleColor.primary).text(StyleColor.light).addInner(new h5().addInner((title)));
        body().id(body_id);
        body().addInner(message);
        footer().addInner(new ModalCloseButton(closeButtonText));
    }
    public MessageDialog(String id,boolean center,String title,String body_id,String message)
    {
        this(id,center,title,body_id,message,"Close");
    }
    public MessageDialog(String id,String title,String message)
    {
        this(id,true,title,null,message);
    }
    public MessageDialog(String title,String message)
    {
        this(null,title,message);
    }
}
