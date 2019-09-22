package org.nova.html.bootstrap4;

public class LinkButton extends ButtonComponent<LinkButton>
{
    public LinkButton(String href)
    {
        super("a");
        attr("href",href);
        attr("role","button");
    }
    /*
    public LinkButton toggle(Collapse collapse)
    {
        attr("data-toggle","collapse");
        attr("data-target","#"+collapse.id());
        return this;
    }
    public LinkButton toggle(Collapse collapse,String collapseClass)
    {
        attr("data-toggle","collapse");
        attr("data-target","."+collapseClass);
        return this;
    }

    //?? test this out
    public LinkButton toggle()
    {
        attr("data-toggle","button");
        return this;
    }
    */
    
    public LinkButton target(String target)
    {
        attr("target",target);
        return this;
    }
}
