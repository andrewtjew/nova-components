package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.target;

public class base extends GlobalEventTagElement<base>
{
    public base()
    {
        super("base",true);
    }
    
    public base href(String URL)
    {
        return attr("href",URL);
    }
    public base target(target target)
    {
        return attr("target",target.toString());
    }
    public base target(String framename)
    {
        return attr("target",framename);
    }
}
