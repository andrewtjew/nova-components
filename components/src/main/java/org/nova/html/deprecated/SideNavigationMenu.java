package org.nova.html.deprecated;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.TagElement;
import org.nova.html.enums.link_rel;
import org.nova.html.ext.Head;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.link;

public class SideNavigationMenu extends div
{
    final private String id;
    final private int width;
    public SideNavigationMenu(Head head,String id,int width,String sourcePath,String cssFile)
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }
        if (head!=null)
        {
            head.add(SideNavigationMenu.class.getCanonicalName(),new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
        }
        this.id=id;
        this.width=width;
        id(id);
        addClass("sidenav");
        this.addInner(new a().href("javascript:void(0)").addClass("closebtn").onclick("document.getElementById('"+id+"').style.width = '0';").addInner("&times;"));
    }
    public SideNavigationMenu(Head head,String id,int width)
    {
        this(head,id,width,"/resources/html","/w3c/SideNavigationMenu/style.css");
    }
    public SideNavigationMenu(Head head,int width)
    {
        this(head,null,width);
    }
    public SideNavigationMenu(int width)
    {
        this(null,width);
    }

    public SideNavigationMenu addMenuItem(a a)
    {
        this.addInner(a);
        return this;
    }
    public SideNavigationMenu addMenuItem(String name,String href)
    {
        return addMenuItem(new a().href(href).addInner(name));
    }
    public SideNavigationMenu openBy(GlobalEventTagElement<? extends TagElement<?>> element)
    {
        element.onclick("document.getElementById('"+this.id+"').style.width = '"+this.width+"px';");
        return this;
    }
}