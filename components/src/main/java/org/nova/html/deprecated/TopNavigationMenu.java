//package org.nova.html.deprecated;
//
//import org.nova.html.enums.link_rel;
//import org.nova.html.ext.Head;
//import org.nova.html.tags.a;
//import org.nova.html.tags.div;
//import org.nova.html.tags.link;
//
//public class TopNavigationMenu extends div
//{
//    public TopNavigationMenu(Head head,String id,String sourcePath,String cssFile)
//    {
//        if (id==null)
//        {
//            id=Integer.toString(this.hashCode());
//        }
//
//        id(id).addClass("topnav");
//        if (head!=null)
//        {
//            head.add(TopNavigationMenu.class.getCanonicalName(),new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
//        }
//    }
//    public TopNavigationMenu(Head head,String id)
//    {
//        this(head,id, "/resources/html","/w3c/TopNavigationMenu/topnav.css");
//    }
//    public TopNavigationMenu(Head head)
//    {
//        this(head,null);
//    }
//    
//    public TopNavigationMenu addMenuItem(String name,String href)
//    {
//        this.addInner(new a().href(href).addInner(name));
//        return this;
//    }
//}
