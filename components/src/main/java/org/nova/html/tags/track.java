
package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.kind;

public class track extends GlobalEventTagElement<track>
{
    public track()
    {
        super("track");
    }
    
    public track default_()
    {
        return attr("default");
    }
    public track default_(boolean default_)
    {
        if (default_)
        {
            return attr("default");
        }
        return this;
    }
    public track kind(kind kind)
    {
        return attr("kind",kind);
    }
    public track label(String text)
    {
        return attr("label",text);
    }
    public track src(String URL)
    {
        return attr("src",URL);
    }
    public track srclang(String language_code)
    {
        return attr("srclang",language_code);
    }
        
    
}
