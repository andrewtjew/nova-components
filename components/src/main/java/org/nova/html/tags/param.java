package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.preload;

import com.nimbusds.jose.jwk.KeyType;

public class param extends GlobalEventTagElement<param>
{
    public param()
    {
        super("param");
    }
    
    public param name(String name)
    {
        return attr("name",name);
    }
    public param value(String value)
    {
        return attr("value",value);
    }
    
}
