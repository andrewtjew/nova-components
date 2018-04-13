package org.nova.html.bootstrap4;

import org.nova.html.attributes.Style;
import org.nova.html.bootstrap4.classes.ThemeColor;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.td;
import org.nova.html.tags.thead;
import org.nova.html.tags.tr;

public class TableHeading extends Element
{
    final private thead thead;
    final private tr tr;
    private ThemeColor color;
    private ThemeColor backgroundColor;
    private ThemeColor textColor;
    
    public TableHeading()
    {
        this.thead=new thead();
        this.tr=this.thead.returnAddInner(new tr());
    }
    
    public TableHeading color(ThemeColor value)
    {
        this.color=value;
        return this;
    }
    public TableHeading backgroundColor(ThemeColor value)
    {
        this.backgroundColor=value;
        return this;
    }
    public TableHeading textColor(ThemeColor value)
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
        theadClass.addIf(this.color!=null,"table",this.color);
        theadClass.addIf(this.textColor!=null,"text",this.textColor);
        theadClass.addIf(this.backgroundColor!=null,"bg",this.backgroundColor);
        
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
