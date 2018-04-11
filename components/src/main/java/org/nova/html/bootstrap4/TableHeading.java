package org.nova.html.bootstrap4;

import org.nova.html.attributes.Style;
import org.nova.html.bootstrap4.classes.BackgroundColor;
import org.nova.html.bootstrap4.classes.TableColor;
import org.nova.html.bootstrap4.classes.TextColor;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.td;
import org.nova.html.tags.thead;
import org.nova.html.tags.tr;

public class TableHeading extends Element
{
    final private thead thead;
    final private tr tr;
    private TableColor color;
    private BackgroundColor backgroundColor;
    private TextColor textColor;
    
    public TableHeading()
    {
        this.thead=new thead();
        this.tr=this.thead.returnAddInner(new tr());
    }
    
    public TableHeading color(TableColor value)
    {
        this.color=value;
        return this;
    }
    public TableHeading backgroundColor(BackgroundColor value)
    {
        this.backgroundColor=value;
        return this;
    }
    public TableHeading textColor(TextColor value)
    {
        this.textColor=value;
        return this;
    }
    public TableHeading add(Element element)
    {
        this.tr.addInner(new td().addInner(element));
        return this;
    }
    public TableHeading add(Style style,Element element)
    {
        this.tr.addInner(new td().style(style).addInner(element));
        return this;
    }
    public TableHeading add(Object object)
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
    public TableHeading add(Style style,Object object)
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
    public TableHeading add(td td)
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
        ClassBuilder theadClass=new ClassBuilder();
        theadClass.add(this.color);
        theadClass.add(this.textColor);
        theadClass.add(this.backgroundColor);
        
        theadClass.applyTo(this.thead);
        composer.render(this.thead);
        
    }
    public tr tr()
    {
        return this.tr;
    }
    public thead thead()
    {
        return this.thead;
    }
}
