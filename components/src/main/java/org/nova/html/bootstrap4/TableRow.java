package org.nova.html.bootstrap4;

import org.nova.html.attributes.Style;
import org.nova.html.bootstrap4.classes.ThemeColor;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.td;
import org.nova.html.tags.tr;

public class TableRow extends Element
{
    final private tr tr;
    private ThemeColor tableColor;
    
    public TableRow()
    {
        this.tr=new tr();
    }
    
    public TableRow color(ThemeColor value)
    {
        this.tableColor=value;
        return this;
    }
    public TableRow add(Object...objects)
    {
        for (Object object:objects)
        {
            if (object==null)
            {
                this.tr.addInner(new td());
            }
            else if (object instanceof td)
            {
                this.tr.addInner(object);
            }
            else
            {
                this.tr.addInner(new td().addInner(object.toString()));
            }
        }
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder();
        cb.addIf(this.tableColor!=null, "table",this.tableColor);
        cb.applyTo(this.tr);
        composer.render(this.tr);
    }
    public tr tr()
    {
        return this.tr;
    }
}
