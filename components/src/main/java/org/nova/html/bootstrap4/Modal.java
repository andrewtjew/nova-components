package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;

public class Modal extends Element
{
    final private div div;
    final private div dialogDiv;
    final private div contentDiv;
    final private div bodyDiv;
    private boolean fade;
    private ModalHeader header;
    private ModalFooter footer;
    
    public Modal(String id)
    {
        this.div=new div().id(id);
        this.dialogDiv=this.div.returnAddInner(new div()).class_("modal-dialog");
        this.contentDiv=this.dialogDiv.returnAddInner(new div()).class_("modal-content");
        this.bodyDiv=new div().class_("modal-body");
    }
    
    public Modal fade(boolean value)
    {
        this.fade=value;
        return this;
    }

    public Modal addHeader(ModalHeader header)
    {
        this.header=header;
        return this;
    }
    
    public Modal addFooter(ModalFooter footer)
    {
        this.footer=footer;
        return this;
    }
    
    public Modal add(Element element)
    {
        this.bodyDiv.addInner(element);
        return this;
    }
    

    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder divClass=new ClassBuilder("modal");
        divClass.addIf(this.fade, "fade");
        
        this.contentDiv.addInner(this.header);
        this.contentDiv.addInner(this.bodyDiv);
        this.contentDiv.addInner(this.footer);
        
        composer.render(this.div);
    }
    
}
