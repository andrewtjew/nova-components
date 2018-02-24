package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.NavbarState;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.li;
import org.nova.html.tags.ul;

public class NavbarList extends Element
{
    final private div div;
    final private ul ul;
    NavbarList(String id)
    {
        this.div=new div().class_("collapse navbar-collapse");
        if (id!=null)
        {
            this.div.id(id);
        }
        this.ul=this.div.returnAddInner(new ul().class_("navbar-nav"));
    }
    public NavbarList()
    {
        this(null);
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.div);
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
        cb.addTo(a);
        return this;
    }
    
    public NavbarList add(NavbarDropdown dropdown)
    {
        this.ul.addInner(dropdown);
        return this;
    }


    public NavbarList addForm(Element element)
    {
        this.div.addInner(element);
        return this;
    }
    
    
}
