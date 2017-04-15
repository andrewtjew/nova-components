package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.scope;
import org.nova.html.enums.sorted;

public class th extends GlobalEventTagElement<th>
{
    public th()
    {
        super("th");
    }
    
    public th abbr(String text)
    {
        return attr("abbr",text);
    }
    public th colspan(int number)
    {
        return attr("colspan",number);
    }
    public th headers(String header_id)
    {
        return attr("header_id",header_id);
    }
    public th rowspan(int number)
    {
        return attr("rowspan",number);
    }
    public th scope(scope scope)
    {
        return attr("scope",scope);
    }
    public th sorted(sorted sorted)
    {
        return attr("sorted",sorted);
    }
    
}
