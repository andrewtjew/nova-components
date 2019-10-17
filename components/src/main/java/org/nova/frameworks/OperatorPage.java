package org.nova.frameworks;

import org.nova.html.deprecated.Content;
import org.nova.html.enums.link_rel;
import org.nova.html.ext.DocType;
import org.nova.html.ext.Head;
import org.nova.html.operator.MenuBar;
import org.nova.html.tags.body;
import org.nova.html.tags.div;
import org.nova.html.tags.head;
import org.nova.html.tags.html;
import org.nova.html.tags.link;
import org.nova.html.tags.p;
import org.nova.html.tags.script;
import org.nova.html.tags.style;
import org.nova.html.templating.Document;
import org.nova.html.templating.InsertMarker;
import org.nova.html.templating.Template;

public class OperatorPage extends Document
{
    final private Head head;
    final private Content content;
    
    public OperatorPage(Template template)
    {
        super(template);
        this.head=this.fill("head", new Head());
        this.content=this.fill("content", new Content());
    }
    
    public Head head()
    {
        return head;
    }
    public Content content()
    {
        return content;
    }
    
    static Template buildTemplate(MenuBar menuBar,String name,String hostName) throws Throwable
    {
        Content content=new Content();
        content.addInner(new DocType("html"));
        html html=content.returnAddInner(new html());
        head head=html.returnAddInner(new head());
        body body=html.returnAddInner(new body());

        int bannerHeight=48;
        int zIndex=10000;
        int height=bannerHeight+34;
        head.addInner(new script().src("/resources/html/jquery/jquery-1.9.1.min.js"));
        head.addInner(new style().addInner(".heading {z-index:"+zIndex+";position:fixed;top:0px;left:0px;width:100%;margin:0 auto;font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;height:"+height+"px;}"
        +" .heading-banner{display:flex;width:100%;height:"+bannerHeight+"px;padding:0;margin:0;border:0;background-color:#ddd;}"
        +" .heading-banner-left{height:"+bannerHeight+"px;background-color:#eee;}"
        +" .heading-banner-center{width:100%;height:"+bannerHeight+"px;}"
        +" .heading-banner-right{background-color:#eee;height:"+bannerHeight+"px;}"
        +" .heading-p{margin:2px;white-space:nowrap;padding-left:8px;padding-right:8px;}"
        +" .body-content{padding:8px;}"));
        
        head.addInner(new link().rel(link_rel.stylesheet).type("text/css").href("/resources/html/ServerApplication/style.css"));
        head.addInner(new link().rel(link_rel.stylesheet).type("text/css").href("/resources/html/widgets/MenuBar/style.css"));
        head.addInner(new InsertMarker("head"));
        
        div heading=body.returnAddInner(new div()).addClass("heading");
        div banner=heading.returnAddInner(new div()).addClass("heading-banner");
        div bannerLeft=banner.returnAddInner(new div()).addClass("heading-banner-left");
        bannerLeft.addInner(new div().style("text-align:left;font-size:15px;font-weight:bold;margin:16px;").addInner(name));
        div bannerCenter=banner.returnAddInner(new div()).addClass("heading-banner-center");
        bannerCenter.addInner(new div().style("text-align:center;font-size:24px;font-weight:bold;margin:10px;").addInner(new InsertMarker("title")));
        div bannerRight=banner.returnAddInner(new div()).addClass("heading-banner-right");
        bannerRight.addInner(new div().style("text-align:center;").addInner(new p().addClass("heading-p").addInner(hostName).addInner(new p().addClass("heading-p").addInner(new InsertMarker("now")))));
        
        heading.addInner(new div().addInner(menuBar));
        
        body.returnAddInner(new div()).style("height:"+height+"px;");
        body.returnAddInner(new div()).addClass("body-content").addInner(new InsertMarker("content"));
        return new Template(content);
    }
    

}
