package org.nova.html.widgets.w3c;

import java.io.OutputStream;

import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.TagElement;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.tags.script;
import org.nova.html.tags.span;

public class SideNav extends div
{
    final private String id;
    final private int width;
    public SideNav(String id,int width)
    {
        this(id,width,"/resources/html","/w3c/SideNav/style.css");
    }
    public SideNav(String id,int width,String sourcePath,String cssFile)
    {
        this.id=id;
        this.width=width;
        id(id);
        class_("sidenav");
        this.addInner(new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
        this.addInner(new a().href("javascript:void(0)").class_("closebtn").onclick("document.getElementById('"+id+"').style.width = '0';").addInner("&times;"));
    }

    public SideNav addMenuItem(a a)
    {
        this.addInner(a);
        return this;
    }
    public SideNav addMenuItem(String name,String href)
    {
        return addMenuItem(new a().href(href).addInner(name));
    }
    public SideNav openBy(GlobalEventTagElement<? extends TagElement<?>> element)
    {
        element.onclick("document.getElementById('"+this.id+"').style.width = '"+this.width+"px';");
        return this;
    }
}
