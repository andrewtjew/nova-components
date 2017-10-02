package org.nova.html.widgets;

import java.io.OutputStream;
import java.util.ArrayList;

import org.nova.html.elements.Element;
import org.nova.html.elements.Inner;
import org.nova.html.elements.InnerElement;
import org.nova.html.tags.a;
import org.nova.html.tags.td;
import org.nova.html.tags.tr;

public class Row extends tr implements Inner<Row>
{
    public Row add(String...items)
    {
        for (String item:items)
        {
            addInner(new td().addInner(item));
        }
        return this;
    }
    public Row add(Object...items)
    {
        for (Object item:items)
        {
            addInner(new td().addInner(item));
        }
        return this;
    }
    public Row add(Element...items)
    {
        for (Element item:items)
        {
            addInner(new td().addInner(item));
        }
        return this;
    }
    public Row addWithTitle(String item,String title)
    {
        addInner(new td().addInner(item).title(title));
        return this;
    }
    public Row addWithTitle(Element item,String title)
    {
        addInner(new td().addInner(item).title(title));
        return this;
    }
    public Row addWithUrl(String item,String url,boolean rowOnClick)
    {
        addInner(new td()
                .addInner(
                        new a()
                        .style("text-decoration:none")
                        .href(url)
                        .addInner(item)
                        ));
        if (rowOnClick)
        {
            onclick("window.location='"+url+"'");
        }
        return this;
    }
    public Row addRemoveAndDetailButtons(String removeScript,String detailLocation)
    {
        td data=returnAddInner(new td());
        data.style("width:5em;");
        if (detailLocation!=null)
        {
            data.addInner(new RowDetailButton(detailLocation));
        }
        if (removeScript!=null)
        {
            data.addInner(new RowRemoveButton().onclick(removeScript));
        }
        return this;
        
    }
    public Row addRemoveButton(String removeScript)
    {
        td data=returnAddInner(new td());
        data.addInner(new RowRemoveButton().onclick(removeScript));
        return this;
        
    }
    public Row addDetailButton(String detailLocation)
    {
        td data=returnAddInner(new td());
        data.addInner(new RowDetailButton(detailLocation));
        return this;
    }
    public Row addInner(Element element)
    {
        super.addInner(element);
        return this;
    }
    public Row setInner(Element element)
    {
        super.setInner(element);
        return this;
    }
    public Row addInners(Element...elements)
    {
        super.addInners(elements);
        return this;
    }
    public Row addInner(Object object)
    {
        super.addInner(object);
        return this;
    }
    /*
    public Row onClickLocation(String URL)
    {
        onclick("window.location='"+URL+"'");
        return this;
    }
    */
}
