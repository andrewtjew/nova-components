package org.nova.html.bootstrap4;

public class PageItem extends Component<PageItem>
{
    public PageItem()
    {
        super("li","page-item");
    }
    
    public PageItem(String label,String href)
    {
        this();
        addInner(new PageLink(label,href));
    }
    public PageItem(String label,String href,boolean active,boolean disabled)
    {
        this();
        PageLink link=returnAddInner(new PageLink(label,href));
        if (active)
        {
            link.active();
        }
        if (disabled)
        {
            link.disabled();
        }
    }
    
    
    
}
