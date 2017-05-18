package org.nova.html.widgets;

import java.util.ArrayList;

import org.nova.core.NameValue;
import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.tags.span;

public class NameValueList extends div
{
    final private String seperator;
    final private ArrayList<NameValue<Element>> list;
    private int longest;

    public NameValueList(String seperator)
    {
        style("display:block;");
        this.seperator=seperator;
        this.list=new ArrayList<>();
        this.longest=0;
    }
    public NameValueList add(String name,Element element)
    {
        this.list.add(new NameValue<Element>(name,element));
        if (name!=null)
        {
            if (name.length()>longest)
            {
                this.longest=name.length();
            }
        }
        return this;
    }
    public NameValueList add(String name,Object value)
    {
        if (value!=null)
        {
            return add(name, new Text(value.toString()));
        }
        return add(name, new Text(null));
    }
    @Override
    public void build(Builder builder) throws Throwable
    {
       int width=this.longest/2+1;
       for (NameValue<Element> item:this.list)
       {
           div line=returnAddInner(new div()).style("display:flex;");
           line.addInner(new div().style("width:"+width+"em;").addInner(item.getName()));
           if (seperator!=null)
           {
               line.addInner(seperator);
           }
           line.addInner(item.getValue());
       }
       super.build(builder);
    }
}
