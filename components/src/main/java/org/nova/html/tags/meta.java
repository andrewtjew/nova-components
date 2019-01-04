package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.enums.http_equiv;
import org.nova.html.enums.name;
import org.nova.html.enums.character_set;

public class meta extends GlobalEventTagElement<meta>
{
    public meta()
    {
        super("meta",true);
    }
    
    public meta charset(character_set character_set)
    {
        return attr("charset",character_set);
    }
    public meta content(String text)
    {
        return attr("content",text);
    }
    public meta http_equiv(http_equiv http_equiv)
    {
        return attr("http-equiv",http_equiv);
    }
    public meta http_equiv_content(http_equiv http_equiv,String content)
    {
        attr("http-equiv",http_equiv);
        return attr("content",content);
    }
    public meta name(name name)
    {
        return attr("name",name);
    }
    
}
