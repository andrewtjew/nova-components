package org.nova.html.bootstrap4;

public class ModalCancelButton extends Button
{
    public ModalCancelButton(String text)
    { 
//        color(StyleColor.danger);
        attr("data-dismiss","modal");
        if (text!=null)
        {
            addInner(text);
        }
    }

    public ModalCancelButton()
    {
        this(null);
    }
    
}
