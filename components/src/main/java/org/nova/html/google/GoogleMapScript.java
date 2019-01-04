package org.nova.html.google;

import java.util.ArrayList;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.script;

public class GoogleMapScript extends Element
{
    final private String apiKey;
    final private ArrayList<GoogleMap> maps;
    
    public GoogleMapScript(String apiKey)
    {
        this.apiKey=apiKey;
        this.maps=new ArrayList<>();
    }
    
    public void add(GoogleMap map)
    {
        this.maps.add(map);
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        String function="googleMap";
        StringBuilder sb=new StringBuilder();
        sb.append("function "+function+"() {");
        
        for (GoogleMap map:this.maps)
        {
            String var="_"+map.div.id();
            sb.append("var "+var+" = new google.maps.Map(document.getElementById('"+map.div.id()+"'), {center: {lat:"+map.lattitude+", lng:"+map.longtitude+"},zoom: "+map.zoom+"});");

            for (GoogleMapCircle circle:map.circles)
            {
                sb.append("new google.maps.Circle({strokeColor:'"+circle.strokeColor+"',strokeOpacity:"+circle.strokeOpacity+",strokeWeight:"+circle.strokeWeight+",fillColor:'"+circle.fillColor+"',fillOpacity:"+circle.fillOpacity+",map: "+var+",center:{lat:"+circle.lattitude+",lng:"
                        +circle.longitude+"},radius:"+circle.radius+"});");
            }
        }        
        sb.append('}');
        
        composer.compose(new script().addInner(sb.toString()));
        composer.compose(new script().src("https://maps.googleapis.com/maps/api/js?key="+this.apiKey+"&callback="+function));
        
    }
    

}
