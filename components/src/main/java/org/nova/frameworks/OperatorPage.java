package org.nova.frameworks;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.body;
import org.nova.html.tags.div;
import org.nova.html.tags.head;
import org.nova.html.tags.html;
import org.nova.html.tags.link;
import org.nova.html.tags.p;
import org.nova.html.tags.script;
import org.nova.html.tags.style;
import org.nova.html.widgets.Content;
import org.nova.html.widgets.DocType;
import org.nova.html.widgets.Head;
import org.nova.html.widgets.MenuBar;
import org.nova.html.widgets.templates.InsertKey;
import org.nova.html.widgets.templates.Template;

public class OperatorPage extends Element
{
    final private Template template;
    final private Head head;
    final private Content content;
    
    OperatorPage(Template template)
    {
        this.template=template;
        this.head=new Head();
        template.fill("head", head);
        this.content=new Content();
        template.fill("content", content);
    }
    
    public Head head()
    {
        return head;
    }
    public Content content()
    {
        return content;
    }
    @Override
    public void build(Composer builder) throws Throwable
    {
        this.template.build(builder);
    }
    
    static Template buildTemplate(MenuBar menuBar,String name,String hostName) throws Throwable
    {
        Content content=new Content();
        content.addInner(new DocType("html"));
        html html=content.returnAddInner(new html());
        head head=html.returnAddInner(new head());
        body body=html.returnAddInner(new body());

        int leftWidth=100;
        int rightWidth=200;
        int bannerHeight=48;
        int zIndex=10;
        int height=bannerHeight+34;
        head.addInner(new script().src("/resources/html/jquery/jquery-1.9.1.min.js"));
        head.addInner(new style().addInner(".heading {z-index:"+zIndex+";position:fixed;top:0px;left:0px;width:100%;margin:0 auto;font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;height:"+height+"px;}"
//        +" .heading-banner{position:fixed;left:0px;top:0px;width:100%;height:"+bannerHeight+"px;z-index:"+zIndex+";padding:0;margin:0;border:0;background-color:#ddd;}"
//        +" .heading-banner-left{position:fixed;float:left;height:"+bannerHeight+"px;background-color:#eee;}"
//        +" .heading-banner-center{width:auto;padding-right:"+rightWidth+"px;}"
//        +" .heading-banner-right{background-color:#eee;position:fixed;right:0;top:0;width:"+rightWidth+"px;height:"+bannerHeight+"px;}"

//        +" .heading-banner{position:fixed;left:0px;top:0px;width:100%;height:"+bannerHeight+"px;z-index:"+zIndex+";padding:0;margin:0;border:0;background-color:#ddd;}"
        +" .heading-banner{display:flex;width:100%;height:"+bannerHeight+"px;padding:0;margin:0;border:0;background-color:#ddd;}"
        +" .heading-banner-left{height:"+bannerHeight+"px;background-color:#eee;}"
        +" .heading-banner-center{width:100%;height:"+bannerHeight+"px;}"
        +" .heading-banner-right{background-color:#eee;height:"+bannerHeight+"px;}"
        +" .heading-p{margin:2px;white-space:nowrap;padding-left:8px;padding-right:8px;}"
        +" .body-content{padding:8px;}"));
        
        head.addInner(new link().rel(link_rel.stylesheet).type("text/css").href("/resources/html/ServerApplication/style.css"));
        head.addInner(new link().rel(link_rel.stylesheet).type("text/css").href("/resources/html/widgets/MenuBar/style.css"));
        head.addInner(new InsertKey("head"));
        
        div heading=body.returnAddInner(new div()).class_("heading");
        div banner=heading.returnAddInner(new div()).class_("heading-banner");
        div bannerLeft=banner.returnAddInner(new div()).class_("heading-banner-left");
        bannerLeft.addInner(new div().style("text-align:left;font-size:15px;font-weight:bold;margin:16px;").addInner(name));
        div bannerCenter=banner.returnAddInner(new div()).class_("heading-banner-center");
        bannerCenter.addInner(new div().style("text-align:center;font-size:24px;font-weight:bold;margin:10px;").addInner(new InsertKey("title")));
        div bannerRight=banner.returnAddInner(new div()).class_("heading-banner-right");
        bannerRight.addInner(new div().style("text-align:center;").addInner(new p().class_("heading-p").addInner(hostName).addInner(new p().class_("heading-p").addInner(new InsertKey("now")))));
        
        heading.addInner(new div().addInner(menuBar));
        
        body.returnAddInner(new div()).style("height:"+height+"px;");
        body.returnAddInner(new div()).class_("body-content").addInner(new InsertKey("content"));
        return new Template(content);
    }
    

}
