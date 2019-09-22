package org.nova.html.bootstrap4;

import org.nova.html.elements.Element;
import org.nova.html.tags.li;

public class NavbarNav extends StyleComponent<NavbarNav>
{
    public NavbarNav()
    {
        super("ul","nav navbar-nav");
    }
    public NavbarNav addItem(Element element)
    {
        returnAddInner(new NavItem()).addInner(element);
        return this;
    }

    /*
    public NavbarNav addLink(String label,String href,NavbarState state)
    {
        li li=returnAddInner(new li().addClass("nav-item"));
        a a=li.returnAddInner(new a());
        a.addInner(label).href(href);
        ClassBuilder cb=new ClassBuilder("nav-link");
        cb.add(state);
        a.addClass(cb.toString());
        return this;
    }
    public NavbarNav addLink(String label,String href)
    {
        return addLink(label,href,NavbarState.active);
    }
    
    public NavbarNav add(ButtonDropdown dropdown)
    {
        li li=returnAddInner(new li().addClass("nav-item dropdown"));
        li.returnAddInner(dropdown);
        return this;
    }

    public NavbarNav add(LinkDropdown dropdown)
    {
        li li=returnAddInner(new li().addClass("nav-item dropdown"));
        li.returnAddInner(dropdown);
        return this;
    }
    */
}
