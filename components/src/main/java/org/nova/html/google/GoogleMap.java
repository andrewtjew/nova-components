package org.nova.html.google;

import java.util.ArrayList;

import org.nova.html.attributes.Size;
import org.nova.html.attributes.Style;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;

public class GoogleMap extends Element
{
    final div div;
    final double lattitude;
    final double longtitude;
    final double zoom;
    final ArrayList<GoogleMapCircle> circles;
    
    public GoogleMap(Size width,Size height,double lattitude,double longtitude,double zoom)
    {
        this.div=new div();
        this.div.id();
        div.style(new Style().width(width).height(height));
        this.lattitude=lattitude;
        this.longtitude=longtitude;
        this.zoom=zoom;
        this.circles=new ArrayList<>();
    }
    
    public void add(GoogleMapCircle circle)
    {
        this.circles.add(circle);
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.div);
    }
    

}
