package org.nova.html.widgets.w3c;

import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.img;
import org.nova.html.tags.link;
import org.nova.html.widgets.Head;

public class TopNavigationMenu extends div
{
    public TopNavigationMenu(Head head,String id,String sourcePath,String cssFile)
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }

        id(id).class_("topnav");
        if (head!=null)
        {
            head.add(TopNavigationMenu.class.getCanonicalName(),new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
        }
    }
    public TopNavigationMenu(Head head,String id)
    {
        this(head,id, "/resources/html","/w3c/TopNavigationMenu/topnav.css");
    }
    
    public TopNavigationMenu addMenuItem(String name,String href)
    {
        this.addInner(new a().href(href).addInner(name));
        return this;
    }
}
