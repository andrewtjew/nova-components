package org.nova.html.widgets.w3c;

import org.nova.html.enums.link_rel;
import org.nova.html.ext.Head;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.link;

public class VerticalMenu extends div
{
    public VerticalMenu(Head head,String id,String sourcePath,String cssFile)
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }
        id(id).addClass("verticalmenu");
        if (head!=null)
        {
            head.add(VerticalMenu.class.getCanonicalName(),new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
        }
    }
    public VerticalMenu(Head head,String id)
    {
        this(head,id, "/resources/html","/w3c/VerticalMenu/style.css");
    }
    
    public VerticalMenu addMenuItem(String name,String href)
    {
        this.addInner(new a().href(href).addInner(name));
        return this;
    }
}
