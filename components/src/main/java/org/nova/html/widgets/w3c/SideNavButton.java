package org.nova.html.widgets.w3c;

import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.tags.span;

public class SideNavButton extends div
{
    final private div sideNavDiv;
    final private span button;
    public SideNavButton(String id,int width,int height,String openStateText,String closeStateText,int sideNavWidth,String sourcePath,String cssFile)
    {
        int fontSize=(int)(height*0.7f)-1;
        this.button=returnAddInner(new span()).addInner(openStateText)                
            .style("vertical-align:middle;text-align:center;color:#e1e1e1;background-color: #000;cursor:pointer;position:fixed;z-index:200;top:0;left:0;width:"+width+"px;height:"+height+"px;font-size:"+fontSize+"px;")
        .onclick("var d=document.getElementById('"+id+"');if (d.offsetWidth==0) {d.style.width='"+sideNavWidth+"px';this.innerHTML='"+closeStateText+"';} else {d.style.width='0px';this.innerHTML='"+openStateText+"';}");

        this.sideNavDiv=returnAddInner(new div()).id(id)
                .class_("sidenav")
                .style("margin-top:"+height+"px;")
                .addInner(new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+"/w3c/SideNav/sidenavbutton.css"));
    }
    public SideNavButton(String id,int width,int height,String openStateText,String closeStateText,int sideNavWidth)
    {
        this(id,width,height,openStateText,closeStateText,sideNavWidth,"/resources/html","/w3c/SideNav/sidenavbutton.css");
    }
    public SideNavButton(String id,int sideNavWidth)
    {
        this(id,48,48,"&#9776;","&#128473;",sideNavWidth);
    }
    public SideNavButton addMenuItem(String text,String URL)
    {
        this.sideNavDiv.addInner(new a().addInner(text).href(URL));
        return this;
    }
    public SideNavButton addMenuElement(Element element)
    {
        this.sideNavDiv.addInner(element);
        return this;
    }
    public div sideNavDiv()
    {
        return this.sideNavDiv;
    }
    public span buttonSpan()
    {
        return this.button;
    }
}
