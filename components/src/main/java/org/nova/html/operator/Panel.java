package org.nova.html.operator;

import org.nova.html.attributes.Style;
import org.nova.html.deprecated.Content;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;
import org.nova.html.enums.link_rel;
import org.nova.html.ext.Head;
import org.nova.html.tags.div;
import org.nova.html.tags.link;

public class Panel extends Element
{
    final private div panel;
    final private div content;
    final private div header;
    final private String title;
    final private Content leftHeaderElements;
    final private Content rightHeaderElements;
    
    public Panel(Head head,String class_,String cssFilePath,String title)
    {
        this.title=title;
        this.panel=new div().addClass(class_);
        this.header=this.panel.returnAddInner(new div().addClass(class_+"-Heading"));
        this.content=this.panel.returnAddInner(new div().addClass(class_+"-Content"));
        if (head!=null)
        {
            head.add(class_,new link().rel(link_rel.stylesheet).type("text/css").href(cssFilePath));
        }
        this.rightHeaderElements=new Content();
        this.leftHeaderElements=new Content();
    }
    
    public InnerElement<?> content()
    {
        return this.content;
    }
    public InnerElement<?> header()
    {
        return this.header;
    }
    public Panel style(Style style)
    {
        this.content.style(style);
        return this;
    }
    public Panel style(String style)
    {
        this.content.style(style);
        return this;
    }
    
    public Panel addRightInHeader(Element element)
    {
        this.rightHeaderElements.addInner(new div().addInner(element).style("float:right"));
        return this;
    }
    public Panel addLeftInHeader(Element element)
    {
        this.leftHeaderElements.addInner(new div().addInner(element).style("float:left"));
        return this;
    }
    @Override
    public void compose(Composer composer) throws Throwable
    {
        this.header.addInner(this.leftHeaderElements);
        this.header.addInner(title);
        this.header.addInner(this.rightHeaderElements);
        composer.compose(this.panel);
        
    }
}
