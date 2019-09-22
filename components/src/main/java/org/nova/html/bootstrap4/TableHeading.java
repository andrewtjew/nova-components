package org.nova.html.bootstrap4;

import org.nova.html.attributes.Style;
import org.nova.html.elements.Element;
import org.nova.html.tags.th;
import org.nova.html.tags.tr;

public class TableHeading extends StyleComponent<TableHeading>
{
    final private tr tr;
    
    public TableHeading()
    {
        super("thead","table");
        this.tr=returnAddInner(new tr());
    }
    public TableHeading light()
    {
        addClass("thead-light");
        return this;
    }
    public TableHeading dark()
    {
        addClass("thead-dark");
        return this;
    }
    public TableHeading add(Style style,Element element)
    {
        this.tr.addInner(new th().style(style).addInner(element));
        return this;
    }
    public TableHeading add(Object object)
    {
        if (object==null)
        {
            this.tr.addInner(new th());
        }
        else if (object instanceof th)
        {
            this.tr.addInner(object);
        }
        else
        {
            this.tr.addInner(new th().addInner(object.toString()));
        }
        return this;
    }
    public TableHeading add(Object...objects)
    {
        for (Object object:objects)
        {
            add(object);
        }
        return this;
    }
    public TableHeading add(Style style,Object object)
    {
        if (object==null)
        {
            this.tr.addInner(new th());
        }
        else
        {
            this.tr.addInner(new th().style(style).addInner(object.toString()));
        }
        return this;
    }

}
