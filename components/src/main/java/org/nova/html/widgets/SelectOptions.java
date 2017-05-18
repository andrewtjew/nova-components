package org.nova.html.widgets;

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
    public SelectOptions addOption(String value,String text,boolean selected)
    {
        addInner(new option().value(value).addInner(text).selected(selected));
        return this;
    }
    public SelectOptions addOption(String value,boolean selected)
    {
        return addOption(value,value,selected);
    }
    public SelectOptions addOption(Object value,boolean selected)
    {
        String text=value.toString();
        return addOption(text,text,selected);
    }
    public SelectOptions addOption(String value,String text)
    {
        addInner(new option().value(value).addInner(text));
        return this;
    }
    public SelectOptions add(String value)
    {
        return addOption(value,value);
    }
    public SelectOptions addOption(Object value)
    {
        String text=value.toString();
        return addOption(text,text);
    }
}
