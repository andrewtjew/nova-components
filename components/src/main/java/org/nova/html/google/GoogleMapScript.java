/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
