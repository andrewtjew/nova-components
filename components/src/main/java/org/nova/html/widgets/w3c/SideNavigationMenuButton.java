package org.nova.html.widgets.w3c;

import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.tags.span;

public class SideNavigationMenuButton extends div
{
    final private span button;
    public SideNavigationMenuButton(String id,int width,int height,String openStateText,String closeStateText,int sideNavWidth,String sourcePath,String cssFile)
    {
        int fontSize=(int)(height*0.9f)-1;
        this.button=returnAddInner(new span()).addInner(openStateText)                
            .style("vertical-align:middle;text-align:center;color:#e1e1e1;background-color: #000;cursor:pointer;position:fixed;z-index:200;top:0;left:0;width:"+width+"px;height:"+height+"px;font-size:"+fontSize+"px;")
        .onclick("var d=document.getElementById('"+id+"');if (d.offsetWidth==0) {d.style.width='"+sideNavWidth+"px';this.innerHTML='"+closeStateText+"';} else {d.style.width='0px';this.innerHTML='"+openStateText+"';}");

        id(id).class_("sidenav")
                .style("margin-top:"+height+"px;")
                .addInner(new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
    }
    public SideNavigationMenuButton(String id,int width,int height,String openStateText,String closeStateText,int sideNavWidth)
    {
        this(id,width,height,openStateText,closeStateText,sideNavWidth,"/resources/html","/w3c/SideNavigationMenu/sidenavbutton.css");
    }
    public SideNavigationMenuButton(String id,int width,int height,int sideNavWidth)
    {
        this(id,width,height,"&#8801;","&#xd7;",sideNavWidth,"/resources/html","/w3c/SideNavigationMenu/sidenavbutton.css");
    }
    public SideNavigationMenuButton(String id,int sideNavWidth)
    {
        this(id,48,48,sideNavWidth);
    }
    public SideNavigationMenuButton addMenuItem(String text,String URL)
    {
        this.addInner(new a().addInner(text).href(URL));
        return this;
    }
    public SideNavigationMenuButton addMenuElement(Element element)
    {
        this.addInner(element);
        return this;
    }
    public span buttonSpan()
    {
        return this.button;
    }
}
