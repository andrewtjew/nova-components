package org.nova.html.bootstrap4;

public class NavLink extends ToggleComponent<NavLink>
{
    public NavLink()
    {
        super("a","nav-link");
    }
    public NavLink(String label,String href)
    {
        this();
        attr("href",href);
        addInner(label);
    }
    public NavLink active()
    {
        addClass("active");
        return this;
    }
    public NavLink disabled()
    {
        addClass("disabled");
        return this;
    }
}
