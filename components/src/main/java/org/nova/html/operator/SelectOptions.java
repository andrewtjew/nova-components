package org.nova.html.operator;

import java.io.OutputStream;

import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;
import org.nova.html.tags.option;
import org.nova.html.tags.select;

public class SelectOptions extends select
{
    public SelectOptions()
    {
    }
    public SelectOptions add(String value,String text,boolean selected)
    {
        addInner(new option().value(value).addInner(text).selected(selected));
        return this;
    }
    public SelectOptions add(String value,String text,String currenValue)
    {
        addInner(new option().value(value).addInner(text).selected(value.equals(currenValue)));
        return this;
    }
    public SelectOptions add(String value,boolean selected)
    {
        return add(value,value,selected);
    }
    public SelectOptions add(Object value,boolean selected)
    {
        String text=value.toString();
        return add(text,text,selected);
    }
    public SelectOptions add(String value,String text)
    {
        addInner(new option().value(value).addInner(text));
        return this;
    }
    public SelectOptions add(Object value,String text)
    {
        addInner(new option().value(value).addInner(text));
        return this;
    }
    public SelectOptions add(String value)
    {
        return add(value,value);
    }
    public SelectOptions addOption(Object value)
    {
        String text=value.toString();
        return add(text,text);
    }
}
