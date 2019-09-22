package org.nova.html.bootstrap4;


public class Badge extends StyleComponent<Badge>
{
    public Badge()
    { 
        super("span","badge");
    }
    public Badge pill()
    {
        addClass("badge-pill");
        return this;
    }

    
}
