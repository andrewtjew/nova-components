package org.nova.html.widgets.w3c;

import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.TagElement;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.tags.p;
import org.nova.html.tags.script;
import org.nova.html.tags.span;

public class ModalBox extends div
{
    final private String id;
    final private div box;
    final private div content;
    final private div header;
    final private div footer;
    public ModalBox(String id,String header,String footer,String sourcePath,String cssFile)
    {
        this.id=id;
        id(id);
        class_("modal");
        this.addInner(new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
        this.addInner(new script().addInner("window.onclick=function(event){if (event.target==document.getElementById('"+id+"')){getElementById('"+id+"').style.display='none';}}"));
        this.box=this.returnAddInner(new div()).class_("modal-content");
        this.header=this.box.returnAddInner(new div()).class_("modal-header");
//        this.header.addInner(new span().class_("modal-close").addInner("&#10005;").onclick("document.getElementById('"+this.id+"').style.display='none'"));
        this.header.addInner(new span().class_("modal-close").addInner("&times;").onclick("document.getElementById('"+this.id+"').style.display='none'"));
        this.header.addInner(new span().addInner(header));
        this.content=this.box.returnAddInner(new div()).class_("modal-body");
        if (footer!=null)
        {
            this.footer=this.box.returnAddInner(new div()).class_("modal-footer").addInner(footer);
        }
        else
        {
            this.footer=null;
        }
    }

    public ModalBox setWidth(String width)
    {
        this.box.style("width:"+width+";");
        return this;
    }
    
    public div content()
    {
        return this.content;
    }
    
    public ModalBox(String id,String title,String footer)
    {
        this(id,title,footer,"/resources/html","/w3c/Modal/modal.css");
    }
    
    public ModalBox openBy(GlobalEventTagElement<? extends TagElement<?>> element)
    {
        element.onclick("document.getElementById('"+this.id+"').style.display='block'");
        return this;
    }
}
