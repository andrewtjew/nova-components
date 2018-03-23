package org.nova.html.operator;

import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.tags.style;
import org.nova.html.widgets.Head;

public class ActionBar extends div
{
    public ActionBar(Head head,String id)
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }
        if (head!=null)
        {
            head.add(id, new style().addInner(".optionsBar{width:100%;display:inline-block;background-color:#ddd;padding-top:0.3em;padding-bottom:0.3em;padding-left:0.3em;padding-right:0.3em;}"));
        }
        class_("optionsBar");
    }
    public ActionBar(Head head)
    {
        this(head, null);
    }
    public ActionBar add(Element element)
    {
        addInner(new div().style("float:left;").addInner(element));
        return this;
    }
}
