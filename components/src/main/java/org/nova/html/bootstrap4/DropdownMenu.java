package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;

public class DropdownMenu extends StyleComponent<DropdownMenu>
{
    public DropdownMenu()
    {
        super("div", "dropdown-menu");
    }
    
    public DropdownMenu right()
    {
        addClass("dropdown-menu-right");
        return this;
    }
}
