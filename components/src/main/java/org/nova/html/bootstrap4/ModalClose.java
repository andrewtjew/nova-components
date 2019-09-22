package org.nova.html.bootstrap4;

public class ModalClose extends Button
{
    public ModalClose(String text)
    { 
        addClass("close");
        attr("data-dismiss","modal");
        if (text!=null)
        {
            addInner(text);
        }
    }

    public ModalClose()
    {
        this("X");
    }
    
}
