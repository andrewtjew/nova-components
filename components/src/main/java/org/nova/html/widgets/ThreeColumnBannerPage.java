package org.nova.html.widgets;

import org.nova.html.enums.link_rel;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.tags.style;

public class ThreeColumnBannerPage extends BasicPage
{
    final private div content;
    final private div banner;
    final private div bannerLeft;
    final private div bannerCenter;
    final private div bannerRight;
    
    public ThreeColumnBannerPage(int leftWidth,int rightWidth,int height,int zIndex)
    {
        style style=new style().addInner(".top-banner{position:fixed;left:0px;top:0px;width:100%;height:"+height+"px;z-index:"+zIndex+";padding:0;margin:0;border:0;} .top-banner-left{position:fixed;left:0;width:"+leftWidth+"px;height:"+height+"px;} .top-banner-center{width:auto;height:"+height+"px;padding-left:"+leftWidth+"px;padding-right:"+rightWidth+"px;} .top-banner-right{position:fixed;right:0;top:0;width:"+rightWidth+"px;height:"+height+"px;} .body-content{width:100%;}");
        this.banner=this.body().returnAddInner(new div()).class_("top-banner").addInner(style);
        this.bannerLeft=this.banner.returnAddInner(new div()).class_("top-banner-left");
        this.bannerCenter=this.banner.returnAddInner(new div()).class_("top-banner-center");
        this.bannerRight=this.banner.returnAddInner(new div()).class_("top-banner-right");
        this.body().returnAddInner(new div()).style("height:"+height+"px;");
        this.content=this.body().returnAddInner(new div()).class_("body-content");
    }

    public div banner()
    {
        return this.banner;
    }
    public div bannerLeft()
    {
        return this.bannerLeft;
    }
    public div bannerRight()
    {
        return this.bannerRight;
    }
    public div bannerCenter()
    {
        return this.bannerCenter;
    }
    public div content()
    {
        return this.content;
    }
}
