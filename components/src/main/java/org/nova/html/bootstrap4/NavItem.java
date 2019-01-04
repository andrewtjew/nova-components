package org.nova.html.bootstrap4;

public class NavItem extends StyleComponent<NavItem>
{
    public NavItem()
    {
        super("li","nav-item");
    }
    
    /*
    public NavItem(String label,String href)
    {
        this();
        addInner(new PageLink(label,href));
    }
    public NavItem(String label,String href,boolean active,boolean disabled)
    {
        this();
        NavLink link=returnAddInner(new NavLink(label,href));
        if (active)
        {
            link.active();
        }
        if (disabled)
        {
            link.disabled();
        }
    }
    */
    
    
}
