package org.nova.html.bootstrap4;


public class ListGroupLink extends ToggleComponent<ListGroupLink>
{
    public ListGroupLink(String href)
    { 
        super("a","list-group-item list-group-item-action");
        attr("href",href);
    }
    
}
