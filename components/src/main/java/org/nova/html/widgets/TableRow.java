package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.td;
import org.nova.html.tags.tr;

public class TableRow extends Element
{
    final private tr tr;
    public TableRow()
    {
        this.tr=new tr();
    }
    
    public tr tr()
    {
        return this.tr;
    }
    public TableRow add(Object...items)
    {
        for (Object item:items)
        {
            if (item==null)
            {
                tr.addInner(new td());
            }
            else if (item instanceof td)
            {
                tr.addInner(item);
            }
            else
            {
                tr.addInner(new td().addInner(item));
            }
        }
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.tr);
    }
}
