package org.nova.html.widgets;

import java.io.OutputStream;

import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;
import org.nova.html.tags.td;
import org.nova.html.tags.th;
import org.nova.html.tags.tr;

public class HeadRow extends th
{
    public HeadRow addItems(String...items)
    {
        for (String item:items)
        {
            addInner(new td().addInner(item));
        }
        return this;
    }
    public HeadRow addItems(Object...items)
    {
        for (Object item:items)
        {
            addInner(new td().addInner(item));
        }
        return this;
    }
    public HeadRow addItem(String item,String title)
    {
        addInner(new td().addInner(item).title(title));
        return this;
    }
    public HeadRow onClickLocation(String URL)
    {
        onclick("window.location='"+URL+"'");
        return this;
    }
}
