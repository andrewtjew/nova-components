package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;
import org.nova.html.tags.img;

public class Brand extends Element
{
    final private org.nova.html.tags.a a;
    public Brand(String label,String href,String imageURL,int imageWidth,int imageHeight)
    {
        this.a=new a().addInner(label).href(href).class_("navbar-brand");
        if (imageURL!=null)
        {
            a.addInner(new img().src(imageURL).height(imageWidth).width(imageHeight));
        }
        
    }
    public Brand(String href,String imageURL,int imageWidth,int imageHeight)
    {
        this(null,href,imageURL,imageWidth,imageHeight);
    }
    
    
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.a);
        
    }
    
}
