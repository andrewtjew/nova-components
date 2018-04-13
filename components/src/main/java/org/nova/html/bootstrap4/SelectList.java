package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.ThemeColor;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.tags.label;
import org.nova.html.tags.option;
import org.nova.html.tags.select;


public class SelectList extends Element
{
    final private div div;
    final private select select;
    private ThemeColor backgroundColor;
    private ThemeColor textColor;
    private Integer borderSize;
    
    public SelectList(String id,String label)
    {
        this.div=new div().class_("form-group");
        if (label!=null)
        {
            div.addInner(new label().for_(id).addInner(label));
        }
        this.select=this.div.returnAddInner(new select()).id(id);
    }
   
    public SelectList backgroundColor(ThemeColor value)
    {
        this.backgroundColor=value;
        return this;
    }
    
    public SelectList textColor(ThemeColor value)
    {
        this.textColor=value;
        return this;
    }
    
    public SelectList borderSize(int value)
    {
        this.borderSize=value;
        return this;
    }
    
    public SelectList(String id)
    {
        this(id,null);
    }
    public SelectList add(option option)
    {
        this.select.addInner(option);
        return this;
    }
    public SelectList add(Object option)
    {
        this.select.addInner(new option().addInner(option));
        return this;
    }
    public SelectList add(Object value,Object option)
    {
        this.select.addInner(new option().addInner(option).value(value));
        return this;
    }
    public SelectList add(Object value,Object option,boolean selected)
    {
        this.select.addInner(new option().addInner(option).value(value).selected(selected));
        return this;
    }
    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder("form-control");
        cb.addIf(this.backgroundColor!=null,"bg",this.backgroundColor);
        cb.addIf(this.textColor!=null,"text",this.textColor);
        if (this.borderSize!=null)
        {
            cb.add("border",this.borderSize);
        }
        cb.applyTo(this.select);
        
        composer.render(this.div);
    }
    
    public select select()
    {
        return this.select;
    }
}
