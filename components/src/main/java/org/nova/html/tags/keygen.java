package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import com.nimbusds.jose.jwk.KeyType;

public class keygen extends GlobalEventTagElement<keygen>
{
    public keygen()
    {
        super("keygen",true);
    }
    
    public keygen autofocus()
    {
        return attr("autofocus");
    }
    public keygen autofocus(boolean autofocus)
    {
        if (autofocus)
        {
            return attr("autofocus");
        }
        return this;
    }
    public keygen challenge()
    {
        return attr("challenge");
    }
    public keygen challenge(boolean challenge)
    {
        if (challenge)
        {
            return attr("challenge");
        }
        return this;
    }
    public keygen disabled()
    {
        return attr("disabled");
    }
    public keygen disabled(boolean disabled)
    {
        if (disabled)
        {
            return attr("disabled");
        }
        return this;
    }
    public keygen form(String form_id)
    {
        return attr("form",form_id);
    }
    public keygen keytype(KeyType keytype)
    {
        return attr("keytype",keytype.toString());
    }
    public keygen name(String text)
    {
        return attr("name",text);
    }
    
}
