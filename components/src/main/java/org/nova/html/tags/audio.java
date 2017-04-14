package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.audio_preload;

public class audio extends GlobalEventTagElement<audio>
{
    public audio()
    {
        super("audio");
    }
    
    public audio autoplay()
    {
        return attr("autoplay");
    }
    public audio autoplay(boolean autoplay)
    {
        if (autoplay)
        {
            return attr("autoplay");
        }
        return this;
    }
    public audio controls()
    {
        return attr("controls");
    }
    public audio controls(boolean controls)
    {
        if (controls)
        {
            return attr("controls");
        }
        return this;
    }
    public audio loop()
    {
        return attr("loop");
    }
    public audio loop(boolean loop)
    {
        if (loop)
        {
            return attr("loop");
        }
        return this;
    }
    public audio muted()
    {
        return attr("muted");
    }
    public audio muted(boolean muted)
    {
        if (muted)
        {
            return attr("muted");
        }
        return this;
    }
    public audio preload(audio_preload preload)
    {
        return attr("preload",preload);
    }
    public audio src(String URL)
    {
        return attr("src",URL);
    }

}
