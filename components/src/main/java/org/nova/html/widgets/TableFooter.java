package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.th;
import org.nova.html.tags.tfoot;
import org.nova.html.tags.tr;

public class TableFooter extends Element
{
    final private tfoot tfoot;
    final private tr tr;
    public TableFooter()
    {
        this.tfoot=new tfoot();
        this.tr=this.tfoot.returnAddInner(new tr());
    }
    public TableFooter add(Object...items)
    {
        for (Object item:items)
        {
            tr.addInner(new th().addInner(item));
        }
        return this;
    }
    
    public tfoot tfoot()
    {
        return this.tfoot;
    }
    public TableFooter add(th...items)
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
        composer.render(this.tfoot);
    }
}
