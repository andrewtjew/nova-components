package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.a;
import org.nova.html.tags.link;

public class MoreLink extends Element
{
    final private a a;
    public MoreLink(Head head,String href)
    {
        this.a=new a().href(href);
        String class_="MoreLink";
        this.a.class_(class_);
        link link=new link().rel(link_rel.stylesheet).type("text/css").href("/resources/html/widgets/"+class_+"/style.css");
        head.add(class_,link);
        this.a.addInner("&#9654;");
    }
    
    public a a()
    {
        return a;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.a);
    }

}
