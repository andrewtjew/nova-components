package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;

public class ModalFooter extends Element
{
    final private div div;
    
    public ModalFooter()
    {
        this.div=new div();
    }
    
    public ModalFooter add(Element element)
    {
        this.div.addInner(element);
        return this;
    }
    

    @Override
    public void compose(Composer composer) throws Throwable
    {
        this.div.class_("modal-footer");
        composer.render(this.div);
    }
    
}
