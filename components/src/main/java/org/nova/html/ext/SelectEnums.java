package org.nova.html.ext;

import java.lang.reflect.Field;

import org.nova.html.attributes.Style;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.option;
import org.nova.html.tags.select;

public class SelectEnums<E extends Enum<E>> extends Element
{
    private final select select;
    public SelectEnums(String name,Class<E> enumType)
    {
        this.select=new select();
        this.select.name(name);
        for (Field field:enumType.getDeclaredFields())
        {
            if (field.isEnumConstant())
            {
                this.select.addInner(new option().addInner(field.getName()).value(field.getName()));
            }
        }
    }
    
    public SelectEnums<E> style(Style value)
    {
        this.select.style(value);
        return this;
    }
    public SelectEnums<E> style(String value)
    {
        this.select.style(value);
        return this;
    }
    public SelectEnums<E> size(int size)
    {
        this.select.size(size);
        return this;
    }
    
    public SelectEnums(String name,Class<E> enumType,Enum<E> selected)
    {
        this.select=new select();
        this.select.name(name);
        for (Field field:enumType.getDeclaredFields())
        {
            if (field.isEnumConstant())
            {
                boolean select=selected.toString().equals(field.getName());
                this.select.addInner(new option().addInner(field.getName()).value(field.getName()).selected(select));
            }
        }
    }
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(this.select);
        
    }
    
}
