package org.nova.html.widgets.w3c;

import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.img;
import org.nova.html.tags.link;

public class TopNavigationMenu extends div
{
    public TopNavigationMenu(String id,String sourcePath,String cssFile)
    {
        id(id).class_("topnav");
        this.addInner(new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
    }
    public TopNavigationMenu(String id)
    {
        this(id, "/resources/html","/w3c/TopNavigationMenu/topnav.css");
    }
    
    public TopNavigationMenu addMenuItem(String name,String href)
    {
        this.addInner(new a().href(href).addInner(name));
        return this;
    }
}
