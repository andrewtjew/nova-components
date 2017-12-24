package org.nova.lexing;

public class Source
{
    final private String text;
    final private String location;
    
    public Source(String text,String location)
    {
        this.text=text;
        this.location=location;
    }
    public Source(String text)
    {
        this(text,null);
    }
    public String getText()
    {
        return text;
    }
    public String getLocation()
    {
        return location;
    }
    
    
}
