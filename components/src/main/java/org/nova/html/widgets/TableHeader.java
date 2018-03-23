package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.th;
import org.nova.html.tags.thead;
import org.nova.html.tags.tr;

public class TableHeader extends Element
{
    final private thead thead;
    final private tr tr;
    public TableHeader()
    {
        this.thead=new thead();
        this.tr=this.thead.returnAddInner(new tr());
    }
    public thead thead()
    {
        return this.thead;
    }
    public TableHeader add(Object...items)
    {
        for (Object item:items)
        {
            tr.addInner(new th().addInner(item));
        }
        return this;
    }
    public TableHeader add(th...items)
    {
        for (Object item:items)
        {
            tr.addInner(item);
        }
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.thead);
    }
}
