package org.nova.html.bootstrap4;

import org.nova.html.elements.GlobalTagElement;
import org.nova.html.tags.span;

public class NavbarToggler extends StyleComponent<NavbarToggler>
{
    public NavbarToggler()
    {
        super("button",null);
        addClass("navbar-toggler");
        data("toggle", "collapse");
        addInner(new span().addClass("navbar-toggler-icon"));
    }

    public NavbarToggler toggleCollapse(NavbarCollapse collapse)
    {
        data("toggle","collapse");
        data("target","#"+collapse.id());
        return this;
    }
   
}
