package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.preload;

import com.nimbusds.jose.jwk.KeyType;

public class progress extends GlobalEventTagElement<progress>
{
    public progress()
    {
        super("progress");
    }
    
    public progress max(int number)
    {
        return attr("max",number);
    }
    public progress value(int number)
    {
        return attr("value",number);
    }
    
}
