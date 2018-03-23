package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class td extends GlobalEventTagElement<td>
{
    public td()
    {
        super("td");
    }
    
    public td colspan(int number)
    {
        return attr("colspan",number);
    }
    public td headers(String header_id)
    {
        return attr("header_id",header_id);
    }
    public td rowspan(int number)
    {
        return attr("rowspan",number);
    }
    
}
