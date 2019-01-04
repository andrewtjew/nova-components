package org.nova.html.bootstrap4;

public class ListGroupItem extends StyleComponent<ListGroupItem>
{
    public ListGroupItem()
    {
        super("li","list-group-item");
    }
    
    public ListGroupItem disabled(boolean value)
    {
        if (value)
        {
            addClass("disabled");
        }
        return this;
    }
    public ListGroupItem disabled()
    {
        return disabled(true);
    }
}
