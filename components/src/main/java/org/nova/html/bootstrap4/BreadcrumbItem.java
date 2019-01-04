package org.nova.html.bootstrap4;

public class BreadcrumbItem extends Component<BreadcrumbItem>
{
    public BreadcrumbItem(String label,String href)
    { 
        super("li","breadcrumb-item");
    }
    
    public BreadcrumbItem active()
    { 
        addClass("active");
        return this;
    }
}
