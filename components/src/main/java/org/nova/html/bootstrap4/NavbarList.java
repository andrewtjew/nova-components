package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.Placement;
import org.nova.html.bootstrap4.classes.NavbarState;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.li;
import org.nova.html.tags.ul;
import org.nova.html.widgets.HtmlUtils;

public class NavbarList extends Element
{
    final private ul ul;
    private Placement placement;
    private Integer placementNumber;

    public NavbarList()
    {
        this.ul=new ul();
    }

    public NavbarList placement(Placement placement,Integer number)
    {
        this.placement=placement;
        this.placementNumber=number;
        return this;
    }

    public NavbarList add(Element element)
    {
        this.ul.returnAddInner(new li()).class_("nav-item").addInner(element);
        return this;
    }

    public NavbarList addLink(String label,String href,NavbarState state)
    {
        li li=this.ul.returnAddInner(new li().class_("nav-item"));
        a a=li.returnAddInner(new a());
        a.addInner(label).href(href);
        ClassBuilder cb=new ClassBuilder("nav-link");
        cb.add(state);
        cb.applyTo(a);
        return this;
    }
    public NavbarList addLink(String label,String href)
    {
        return addLink(label,href,NavbarState.active);
    }
    
    public NavbarList add(ButtonDropdown dropdown)
    {
        li li=this.ul.returnAddInner(new li().class_("nav-item dropdown"));
        li.returnAddInner(dropdown);
        return this;
    }

    public NavbarList add(LinkDropdown dropdown)
    {
        li li=this.ul.returnAddInner(new li().class_("nav-item dropdown"));
        li.returnAddInner(dropdown);
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder("nav navbar-nav");
        cb.add(this.placement,this.placementNumber);
        cb.applyTo(this.ul);
        composer.render(this.ul);
    }


    
}
