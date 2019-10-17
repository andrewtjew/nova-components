package org.nova.html.deprecated;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.TagElement;
import org.nova.html.enums.link_rel;
import org.nova.html.ext.Head;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.tags.script;
import org.nova.html.tags.span;

public class ModalBox extends div
{
    final private String id;
    final private div box;
    final private div content;
    final private div header;
    final private div footer;
    public ModalBox(Head head,String id,String header,String footer,String sourcePath,String cssFile)
    {
        this.id=id;
        id(id);
        addClass("modal");
        if (head!=null)
        {
            head.add(ModalBox.class.getCanonicalName(),new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
        }
        this.addInner(new script().addInner("window.onclick=function(event){if (event.target==document.getElementById('"+id+"')){getElementById('"+id+"').style.display='none';}}"));
        this.box=this.returnAddInner(new div()).addClass("modal-content");
        this.header=this.box.returnAddInner(new div()).addClass("modal-header");
        this.header.addInner(new span().addClass("modal-close").addInner("&times;").onclick("document.getElementById('"+this.id+"').style.display='none'"));
        this.header.addInner(new span().addInner(header));
        this.content=this.box.returnAddInner(new div()).addClass("modal-body");
        if (footer!=null)
        {
            this.footer=this.box.returnAddInner(new div()).addClass("modal-footer").addInner(footer);
        }
        else
        {
            this.footer=null;
        }
    }
    public ModalBox(Head head,String id,String title,String footer)
    {
        this(head,id,title,footer,"/resources/html","/w3c/ModalBox/modal.css");
    }
    
    public ModalBox(Head head,String id,String title)
    {
        this(head,id,title,null);
    }

    public div content()
    {
        return this.content;
    }
    
    public div box()
    {
        return this.box;
    }
    
    public ModalBox openBy(GlobalEventTagElement<? extends TagElement<?>> element)
    {
        element.onclick("document.getElementById('"+this.id+"').style.display='block'");
        return this;
    }
}
