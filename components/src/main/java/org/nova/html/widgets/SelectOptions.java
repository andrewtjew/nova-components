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
    public SelectOptions addOption(String value,String text)
    {
        addInner(new option().value(value).addInner(text));
        return this;
    }
    public SelectOptions addOption(String value)
    {
        return addOption(value,value);
    }
}
