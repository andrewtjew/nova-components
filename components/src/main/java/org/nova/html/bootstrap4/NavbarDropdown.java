package org.nova.html.bootstrap4;

import org.nova.html.tags.li;
import org.nova.html.bootstrap4.classes.NavbarState;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.properties.Style;
import org.nova.html.properties.float_;
import org.nova.html.properties.list_style;
import org.nova.html.properties.position;
import org.nova.html.tags.a;
import org.nova.html.tags.div;

public class NavbarDropdown extends Element
{
    final private li li;
    final private div div;
    public NavbarDropdown(String id,String label)
    {
        this.li=new li().class_("nav-item dropdown");
        this.li.style(new Style().position(position.relative).float_(float_.left).list_style(list_style.none));
        a a=new a().class_("nav-link dropdown-toggle").href("#").id(id).attr("role","button").data("toggle","dropdown");
//        a.attr("aria-haspopup","true").attr("aria-expanded","false");
        a.addInner(label);
        li.addInner(a);
        this.div=this.li.returnAddInner(new div().class_("dropdown-menu"));
    }
    
    public NavbarDropdown addLink(String label,String href)
    {
        a a=this.div.returnAddInner(new a().addInner(label).href(href));
        a.class_("dropdown-item");
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.li);
        
    }
}
