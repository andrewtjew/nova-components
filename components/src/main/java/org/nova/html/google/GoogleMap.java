package org.nova.html.google;

import java.util.ArrayList;

import org.nova.html.attributes.Color;
import org.nova.html.attributes.Size;
import org.nova.html.attributes.Style;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.tags.script;
import org.nova.html.widgets.HtmlUtils;

public class GoogleMap extends Element
{
    final private div div;
    final private double lattitude;
    final private double longtitude;
    final private double zoom;
    final private String apiKey;
    final private ArrayList<GoogleMapCircle> circles;
    
    
    
    public GoogleMap(String apiKey,Size width,Size height,double lattitude,double longtitude,double zoom)
    {
        this.div=new div();
        HtmlUtils.autoId(this.div);
        div.style(new Style().width(width).height(height));
        this.lattitude=lattitude;
        this.longtitude=longtitude;
        this.zoom=zoom;
        this.apiKey=apiKey;
        this.circles=new ArrayList<>();
    }
    
    public void add(GoogleMapCircle circle)
    {
        this.circles.add(circle);
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.div);
        String function="map"+this.div.id();
        StringBuilder sb=new StringBuilder();
        sb.append("function "+function+"() { var map = new google.maps.Map(document.getElementById('"+this.div.id()+"'), {center: {lat:"+this.lattitude+", lng:"+this.longtitude+"},zoom: "+this.zoom+"});");

        for (GoogleMapCircle circle:this.circles)
        {
            sb.append("var cityCircle = new google.maps.Circle({strokeColor:'"+circle.strokeColor+"',strokeOpacity:"+circle.strokeOpacity+",strokeWeight:"+circle.strokeWeight+",fillColor:'"+circle.fillColor+"',fillOpacity:"+circle.fillOpacity+",map: map,center:{lat:"+circle.lattitude+",lng:"
                    +circle.longitude+"},radius:"+circle.radius+"});");
        }
        
        sb.append('}');
        
        
        composer.render(new script().addInner(sb.toString()));
        composer.render(new script().src("https://maps.googleapis.com/maps/api/js?key="+this.apiKey+"&callback="+function));
        
    }
    

}
