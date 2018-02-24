package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.TableColor;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.properties.Style;
import org.nova.html.tags.td;
import org.nova.html.tags.tr;

public class TableRow extends Element
{
    final private tr tr;
    private TableColor tableColor;
    
    public TableRow()
    {
        this.tr=new tr();
    }
    
    public TableRow color(TableColor value)
    {
        this.tableColor=value;
        return this;
    }
    public TableRow add(Element element)
    {
        this.tr.addInner(new td().addInner(element));
        return this;
    }
    public TableRow add(Style style,Element element)
    {
        this.tr.addInner(new td().style(style).addInner(element));
        return this;
    }
    public TableRow add(Object object)
    {
        if (object==null)
        {
            this.tr.addInner(new td());
        }
        else
        {
            this.tr.addInner(new td().addInner(object.toString()));
        }
        return this;
    }
    public TableRow add(Style style,Object object)
    {
        if (object==null)
        {
            this.tr.addInner(new td());
        }
        else
        {
            this.tr.addInner(new td().style(style).addInner(object.toString()));
        }
        return this;
    }
    public TableRow add(td td)
    {
        if (td==null)
        {
            this.tr.addInner(new td());
        }
        else
        {
            this.tr.addInner(td);
        }
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        if (this.tableColor!=null)
        {
            this.tr.class_(this.tableColor.toString());
        }
        composer.render(this.tr);
        // TODO Auto-generated method stub
        
    }
    public tr tr()
    {
        return this.tr;
    }
}
