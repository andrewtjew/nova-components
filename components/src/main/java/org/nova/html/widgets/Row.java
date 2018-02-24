package org.nova.html.widgets;

import java.io.OutputStream;
import java.util.ArrayList;

import org.nova.html.elements.Element;
import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.InnerElement;
import org.nova.html.tags.a;
import org.nova.html.tags.td;
import org.nova.html.tags.tr;
import org.nova.html.xtags.remove_button;
import org.nova.html.xtags.more_button;

public class Row extends GlobalEventTagElement<Row>
{
    public Row()
    {
        super("tr");
    }
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
            data.addInner(new more_button(detailLocation));
        }
        if (removeScript!=null)
        {
            data.addInner(new remove_button().onclick(removeScript));
        }
        return this;
        
    }
    public Row addRemoveButton(String removeScript)
    {
        td data=returnAddInner(new td());
        data.addInner(new remove_button().onclick(removeScript));
        return this;
        
    }
    public Row addDetailButton(String detailLocation)
    {
        td data=returnAddInner(new td());
        data.addInner(new more_button(detailLocation));
        return this;
    }
    
}
