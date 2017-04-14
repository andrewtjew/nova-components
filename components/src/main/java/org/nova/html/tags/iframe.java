package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.area_rel;
import org.nova.html.enums.sandbox;
import org.nova.html.enums.area_shape;
import org.nova.html.enums.target;

public class iframe extends GlobalEventTagElement<iframe>
{
    public iframe()
    {
        super("iframe");
    }
    
    public iframe height(int height)
    {
        return attr("height",height);
    }
    public iframe name(String text)
    {
        return attr("name",text);
    }
    public iframe sandbox(sandbox sandbox)
    {
        return attr("name",sandbox);
    }
    
    public iframe src(String URL)
    {
        return attr("src",URL);
    }
    public iframe srcdoc(String HTML_code)
    {
        return attr("srcdoc",HTML_code);
    }
    public iframe width(int width)
    {
        return attr("width",width);
    }
    
}
