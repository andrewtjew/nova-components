package org.nova.html.tags;

import org.nova.html.elements.GlobalEventTagElement;

public class html extends GlobalEventTagElement<html>
{
    public html()
    {
        super("html");
    }
    public html manifest(String URL)
    {
        return attr("manifest",URL);
    }
    public html xmlns()
    {
        return attr("xmlns","http://www.w3.org/1999/xhtml");
    }
    
}
