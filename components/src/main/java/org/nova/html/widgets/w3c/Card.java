package org.nova.html.widgets.w3c;

import org.nova.html.enums.link_rel;
import org.nova.html.tags.div;
import org.nova.html.tags.link;
import org.nova.html.tags.img;

public class Card extends div
{
    final private img img;
    final private div content;
    
    public Card(String id,String sourcePath,String cssFile)
    {
        id(id).class_("card");
        this.addInner(new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
        this.img=this.returnAddInner(new img()).style("width:100%");
        this.content=this.returnAddInner(new div()).class_("container");
    }
    public Card(String id)
    {
        this(id, "/resources/html","/w3c/Card/card.css");
    }

    public Card setDimensions(int width,int height)
    {
        this.style("width:"+width+"px;height:"+height+"px;");
        return this;
    }
    
    public Card setDimensions(String width,String height)
    {
        this.style("width:"+width+";height:"+height+";");
        return this;
    }
    
    public div content()
    {
        return this.content;
    }
 
    public img img()
    {
        return this.img;
    }
    
 }
