package org.nova.html.widgets;

import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.tags.head;
import org.nova.html.tags.style;

public class OptionsBar extends div
{
    public OptionsBar(Head head,String id)
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }
        if (head!=null)
        {
            head.add(id, new style().addInner(".optionsBar{width:100%;display:inline-block;background-color:#ddd;padding-top:0.5em;padding-bottom:0.5em;}"));
        }
        class_("optionsBar");
    }
    public OptionsBar(Head head)
    {
        this(head, null);
    }
    public OptionsBar add(Element element)
    {
        addInner(new div().style("float:left;").addInner(element));
        return this;
    }
}
