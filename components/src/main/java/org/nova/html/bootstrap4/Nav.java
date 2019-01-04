package org.nova.html.bootstrap4;

public class Nav extends Component<Nav>
{
    public Nav()
    {
        super("ul","nav");
    }

    public Nav tabs()
    {
        return addClass("nav-tabs");
    }
    public Nav pills()
    {
        return addClass("nav-pills");
    }
    public Nav fill()
    {
        return addClass("nav-fill");
    }
    public Nav justified()
    {
        return addClass("nav-justified");
    }
}
