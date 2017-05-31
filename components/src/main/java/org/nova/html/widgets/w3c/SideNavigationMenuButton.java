package org.nova.html.widgets.w3c;

import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.properties.unit;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.tags.span;
import org.nova.html.widgets.Head;

public class SideNavigationMenuButton extends div
{
    final private span button;
    public SideNavigationMenuButton(Head head,String id,int buttonWidth,int buttonHeight,unit buttonUnit,String openStateText,String closeStateText,int sideNavWidth,unit sideBarWidthUnit,String sourcePath,String cssFile)
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }
        this.button=new span().addInner(openStateText)     
            .class_("sidenavbutton")
//            .style("vertical-align:middle;text-align:center;color:#e1e1e1;background-color: #000;cursor:pointer;position:fixed;z-index:200;top:0;left:0;width:"+buttonWidth+buttonUnit+";height:"+buttonHeight+buttonUnit+";font-size:"+fontSize+buttonUnit+";")
            .style("width:"+buttonWidth+buttonUnit+";height:"+buttonHeight+buttonUnit)
        .onclick("var d=document.getElementById('"+id+"');if (d.offsetWidth==0) {d.style.width='"+sideNavWidth+sideBarWidthUnit+"';this.innerHTML='"+closeStateText+"';} else {d.style.width='0px';this.innerHTML='"+openStateText+"';}");

        id(id).class_("sidenav")
                .style("margin-top:"+buttonHeight+"px;");
        
        head.add(SideNavigationMenuButton.class.getCanonicalName(),new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
    }
    public SideNavigationMenuButton(Head head,String id,int width,int height,unit buttonSizeUnit,String openStateText,String closeStateText,int sideNavWidth,unit sideNavWidthUnit)
    {
        this(head,id,width,height,buttonSizeUnit,openStateText,closeStateText,sideNavWidth,sideNavWidthUnit,"/resources/html","/w3c/SideNavigationMenu/sidenavbutton.css");
    }
    public SideNavigationMenuButton(Head head,String id,int width,int height,int sideNavWidth,unit sizeUnit)
    {
        this(head,id,width,height,sizeUnit,"&#8801;","&#x2716",sideNavWidth,sizeUnit,"/resources/html","/w3c/SideNavigationMenu/sidenavbutton.css");
    }
    public SideNavigationMenuButton(Head head,String id,int size,int sideNavWidth,unit sizeUnit)
    {
        this(head,id,size,size,sideNavWidth,sizeUnit);
    }
    public SideNavigationMenuButton(Head head,int size,int sideNavWidth,unit sizeUnit)
    {
        this(head,null,size,size,sideNavWidth,sizeUnit);
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
