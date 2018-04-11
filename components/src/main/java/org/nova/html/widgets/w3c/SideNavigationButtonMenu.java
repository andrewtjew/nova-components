package org.nova.html.widgets.w3c;

import org.nova.html.attributes.unit;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.tags.span;
import org.nova.html.widgets.Head;

public class SideNavigationButtonMenu extends div
{
    final private span button;
    public SideNavigationButtonMenu(Head head,String id,int buttonWidth,int buttonHeight,unit buttonUnit,String openStateText,String closeStateText,int sideNavWidth,unit sideBarWidthUnit,String sourcePath,String cssFile)
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
        
        head.add(SideNavigationButtonMenu.class.getCanonicalName(),new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
    }
    public SideNavigationButtonMenu(Head head,String id,int buttonWidth,int buttonHeight,unit buttonSizeUnit,String openStateText,String closeStateText,int sideNavWidth,unit sideNavWidthUnit)
    {
        this(head,id,buttonWidth,buttonHeight,buttonSizeUnit,openStateText,closeStateText,sideNavWidth,sideNavWidthUnit,"/resources/html","/w3c/SideNavigationMenu/sidenavbutton.css");
    }
    public SideNavigationButtonMenu(Head head,String id,int buttonWidth,int buttonHeight,int sideNavWidth,unit sizeUnit)
    {
        this(head,id,buttonWidth,buttonHeight,sizeUnit,"&#8801;","&#x2716",sideNavWidth,sizeUnit,"/resources/html","/w3c/SideNavigationMenu/sidenavbutton.css");
    }
    public SideNavigationButtonMenu(Head head,String id,int buttonSize,int sideNavWidth,unit sizeUnit)
    {
        this(head,id,buttonSize,buttonSize,sideNavWidth,sizeUnit);
    }
    public SideNavigationButtonMenu(Head head,int size,int sideNavWidth,unit sizeUnit)
    {
        this(head,null,size,size,sideNavWidth,sizeUnit);
    }
    public SideNavigationButtonMenu addMenuItem(String text,String URL)
    {
        this.addInner(new a().addInner(text).href(URL));
        return this;
    }
    public SideNavigationButtonMenu addMenuElement(Element element)
    {
        this.addInner(element);
        return this;
    }
    public span buttonSpan()
    {
        return this.button;
    }
}
