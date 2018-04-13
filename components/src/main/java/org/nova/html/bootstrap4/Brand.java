package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.Placement;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;
import org.nova.html.tags.img;

public class Brand extends Element
{
    final private org.nova.html.tags.a a;
    private Placement placement;
    private Integer placementNumber;
    private Integer width;
    
    public Brand(String label,String href,String imageURL,int imageWidth,int imageHeight)
    {
        this.a=new a().addInner(label).href(href);
        if (imageURL!=null)
        {
            a.addInner(new img().src(imageURL).height(imageWidth).width(imageHeight));
        }
        
    }
    public Brand(String href,String imageURL,int imageWidth,int imageHeight)
    {
        this(null,href,imageURL,imageWidth,imageHeight);
    }
    
    public Brand(String label,String href)
    {
        this(label,href,null,0,0);
    }
    
    public Brand width(Integer value)
    {
        this.width=value;
        return this;
    }
    public Brand placement(Placement value,Integer number)
    {
        this.placement=value;
        this.placementNumber=number;
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder("navbar-brand");
        cb.add(this.placement,this.placementNumber);
        cb.addIf(this.width!=null, "w",this.width);
        cb.applyTo(this.a);
        composer.render(this.a);
    }
    
}
