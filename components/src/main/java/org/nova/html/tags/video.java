package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.preload;

public class video extends GlobalEventTagElement<video>
{
    public video()
    {
        super("audio");
    }
    
    public video autoplay()
    {
        return attr("autoplay");
    }
    public video autoplay(boolean autoplay)
    {
        if (autoplay)
        {
            return attr("autoplay");
        }
        return this;
    }
    public video controls()
    {
        return attr("controls");
    }
    public video controls(boolean controls)
    {
        if (controls)
        {
            return attr("controls");
        }
        return this;
    }
    public video height(String pixels)
    {
        return attr("height",pixels);
    }
    public video loop()
    {
        return attr("loop");
    }
    public video loop(boolean loop)
    {
        if (loop)
        {
            return attr("loop");
        }
        return this;
    }
    public video muted()
    {
        return attr("muted");
    }
    public video muted(boolean muted)
    {
        if (muted)
        {
            return attr("muted");
        }
        return this;
    }
    public video poster(String URL)
    {
        return attr("poster",URL);
    }
    public video preload(preload preload)
    {
        return attr("preload",preload);
    }
    public video src(String URL)
    {
        return attr("src",URL);
    }
    public video width(String pixels)
    {
        return attr("width",pixels);
    }
    

}
