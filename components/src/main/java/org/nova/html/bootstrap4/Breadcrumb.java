package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;
import org.nova.html.tags.nav;

public class Breadcrumb extends StyleComponent<Breadcrumb>
{
    public Breadcrumb()
    {
        super("ol", "breadcrumb");
    }   
    
    public Breadcrumb addItemInline(String title,String href,boolean active)
    {
        BreadcrumbItem item=new BreadcrumbItem(title,href);
        if (active)
        {
            item.active();
        }
        return addInner(item);
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        InnerElement<?> parent=new nav();
        
        parent.addInner(new Element()
        {
            @Override
            public void compose(Composer composer) throws Throwable
            {
                composeThis(composer);
            }
        });
        parent.compose(composer);
        
    }
}
