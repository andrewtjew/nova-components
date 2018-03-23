package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;

public class ModalHeader extends Element
{
    final private div div;
    
    public ModalHeader()
    {
        this.div=new div();
    }
    
    public ModalHeader add(Element element)
    {
        this.div.addInner(element);
        return this;
    }
    
    public ModalHeader addCloseButton(String label)
    {
        this.div.returnAddInner(new button_button()).class_("close").attr("data-dismiss","modal").addInner(label);
        return this;
    }
    
    public ModalHeader addCloseButton()
    {
        return addCloseButton("&times;");
    }
    

    @Override
    public void compose(Composer composer) throws Throwable
    {
        this.div.class_("modal-header");
        composer.render(this.div);
    }
    
}
