package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;

public class CarouselItem extends StyleComponent<CarouselItem>
{
    private CarouselCaption caption;
    boolean active;
    
    public CarouselItem(String src)
    {
        this(src,null);
    }
    public CarouselItem(String src,String alt)
    {
        this(new Image(src,alt).w(100));
    }
    public CarouselItem(Image image)
    {
        super("div","carousel-item");
        returnAddInner(image);
    }
    public CarouselItem set(CarouselCaption caption)
    {
        this.addInner(caption);
        return this;
    }
    public CarouselItem active()
    {
        this.active=true;
        addClass("active");
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        super.compose(composer);
    }
    
    
}
