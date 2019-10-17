package org.nova.html.operator;

import org.nova.html.deprecated.RemoveButton;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.ext.Head;

public class RemoveAndDetailButtons extends Element
{
    final private MoreButton link;
    final private RemoveButton remove;
    public RemoveAndDetailButtons(Head head,String remove,String details)
    {
        this.remove=new RemoveButton(remove);
        this.link=new MoreButton(head, details);
        this.link.style("float:right;");
    }
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.link);
        composer.compose(this.remove);
    }
}
