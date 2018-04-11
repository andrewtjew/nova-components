package org.nova.html.widgets;

import org.nova.html.attributes.Style;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.link;

public class LinkButton extends Element
{
    final private a a;
    public LinkButton(Head head,String href,String label,String title,String cssPath)
    {
        this.a=new a().href(href);
        String class_="MoreLink";
        this.a.class_(class_);
        link link=new link().rel(link_rel.stylesheet).type("text/css").href(cssPath);
        head.add(class_,link);
        this.a.addInner(label);
        this.a.title(title);
    }
    public LinkButton(Head head,String href,String label,String title)
    {
        this(head,href,label,title,"/resources/html/widgets/LinkButton/style.css");
    }
    public LinkButton(Head head,String href,String label)
    {
        this(head,href,label,null);
    }
    
    public LinkButton style(String style)
    {
        a.style(style);
        return this;
    }

    public LinkButton style(Style style)
    {
        a.style(style);
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.a);
    }

}
