package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.preload;

import com.nimbusds.jose.jwk.KeyType;

public class li extends GlobalEventTagElement<li>
{
    public li()
    {
        super("li");
    }
    
    public li value(int value)
    {
        return attr("value",value);
    }
    
}
