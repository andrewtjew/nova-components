package org.nova.html.bootstrap4;

public class PageLink extends Component<PageLink>
{
    public PageLink()
    {
        super("a","page-link");
    }
    public PageLink(String label,String href)
    {
        this();
        attr("href",href);
        addInner(label);
    }
    public PageLink active()
    {
        addClass("active");
        return this;
    }
    public PageLink disabled()
    {
        addClass("disabled");
        return this;
    }
}
