package org.nova.html.bootstrap4;

public class ListGroup extends StyleComponent<ListGroup>
{
    public ListGroup()
    {
        super("ul","list-group");
    }

    public ListGroup flush()
    {
        addClass(this.getComponentClass(),"flush");
        return this;
    }
}
