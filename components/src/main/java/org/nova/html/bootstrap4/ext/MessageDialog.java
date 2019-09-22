package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.ModalCloseButton;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.tags.h5;

public class MessageDialog extends ModalDocument
{
    public MessageDialog(boolean center,String title,String message,String closeButtonText)
    {
        super(center);
        header().bg(StyleColor.primary).text(StyleColor.light).addInner(new h5().addInner((title)));
        body().addInner(message);
        footer().addInner(new ModalCloseButton(closeButtonText));
    }
    public MessageDialog(boolean center,String title,String message)
    {
        this(center,title,message,"Close");
    }
}
