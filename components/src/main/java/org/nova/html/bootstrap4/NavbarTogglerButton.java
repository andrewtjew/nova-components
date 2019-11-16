package org.nova.html.bootstrap4;

import org.nova.html.elements.GlobalTagElement;
import org.nova.html.tags.span;

public class NavbarTogglerButton extends ButtonComponent<NavbarTogglerButton>
{
    public NavbarTogglerButton()
    {
        this(true);
    }
    public NavbarTogglerButton(boolean togglerIcon)
    {
        super("button");
        addClass("navbar-toggler");
        data("toggle", "collapse");
        if (togglerIcon)
        {
            addInner(new span().addClass("navbar-toggler-icon"));
        }
    }

    @Deprecated
    public NavbarTogglerButton toggleCollapse(NavbarCollapse collapse)
    {
        data("toggle","collapse");
        data("target","#"+collapse.id());
        return this;
    }
   
}
