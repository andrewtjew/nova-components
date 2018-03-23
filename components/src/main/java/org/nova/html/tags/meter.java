package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class meter extends GlobalEventTagElement<meter>
{
    public meter()
    {
        super("meter");
    }
    
    public meter form(String form_id)
    {
        return attr("form",form_id);
    }
    public meter high(int number)
    {
        return attr("high",number);
    }
    public meter low(int number)
    {
        return attr("low",number);
    }
    public meter max(int number)
    {
        return attr("max",number);
    }
    public meter min(int number)
    {
        return attr("min",number);
    }
    public meter optimum(int number)
    {
        return attr("optimum",number);
    }
    public meter value(int number)
    {
        return attr("value",number);
    }
    
}
