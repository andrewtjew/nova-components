package org.nova.html.bootstrap4;

public class NavItem extends StyleComponent<NavItem>
{
    public NavItem()
    {
        super("li","nav-item");
    }
    
    public NavItem dropdown()
    {
        addClass("dropdown");
        return this;
    }
}
