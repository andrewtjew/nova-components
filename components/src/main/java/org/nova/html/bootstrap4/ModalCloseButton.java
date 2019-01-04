package org.nova.html.bootstrap4;

public class ModalCloseButton extends Button
{
    public ModalCloseButton(String text)
    { 
//        color(StyleColor.secondary);
        attr("data-dismiss","modal");
        if (text!=null)
        {
            addInner(text);
        }
    }

    public ModalCloseButton()
    {
        this(null);
    }
    
}
