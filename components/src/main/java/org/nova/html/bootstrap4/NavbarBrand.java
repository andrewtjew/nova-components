package org.nova.html.bootstrap4;

import org.nova.html.tags.img;

public class NavbarBrand extends StyleComponent<NavbarBrand>
{
    public NavbarBrand(String label,String href,String imageURL,int imageWidth,int imageHeight)
    {
        super("a","navbar-brand");
        addInner(label);
        attr("href",href);
        if (imageURL!=null)
        {
            addInner(new img().src(imageURL).width(imageWidth).height(imageHeight));
        }
        
    }
    public NavbarBrand(String href,String imageURL,int imageWidth,int imageHeight)
    {
        this(null,href,imageURL,imageWidth,imageHeight);
    }
    
    public NavbarBrand(String label,String href)
    {
        this(label,href,null,0,0);
    }
    
    
}
