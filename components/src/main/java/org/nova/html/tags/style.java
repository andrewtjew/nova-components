
package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class style extends GlobalEventTagElement<style>
{
    public style()
    {
        super("style");
    }
    
    public style media(String media_query)
    {
        return attr("media",media_query);
    }
    public style scoped()
    {
        return attr("scoped");
    }
    public style scoped(boolean scoped)
    {
        if (scoped)
        {
            return attr("scoped");
        }
        return this;
    }
   public style type(String type)
    {
        return attr("type",type);
    }
        
    
}
