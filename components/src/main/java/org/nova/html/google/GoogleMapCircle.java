package org.nova.html.google;

public class GoogleMapCircle
{
    final String strokeColor;
    final double strokeOpacity;
    final double strokeWeight;
    final String fillColor;
    final double fillOpacity;
    final double longitude;
    final double lattitude;
    final  double radius;
    
    public GoogleMapCircle(double lattitude,double longitude,double radius,String strokeColor,double strokeOpacity,double strokeWeight,String fillColor,double fillOpacity)
    {
        this.longitude=longitude;
        this.lattitude=lattitude;
        this.radius=radius;
        this.strokeColor=strokeColor;
        this.strokeOpacity=strokeOpacity;
        this.strokeWeight=strokeWeight;
        this.fillColor=fillColor;
        this.fillOpacity=fillOpacity;
    }
}
