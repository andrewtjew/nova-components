package org.nova.html.widgets;

import java.util.ArrayList;

import org.nova.core.NameValue;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.tags.span;

public class NameValueList extends div
{
    final private ArrayList<NameValue<Element>> list;
    private int longest;

    public NameValueList()
    {
        style("display:block;");
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
    public void compose(Composer builder) throws Throwable
    {
       int width=(int)(this.longest*0.7f+1);
       for (int i=0;i<this.list.size();i++)
       {
           NameValue<Element> item=this.list.get(i);

           div line=returnAddInner(new div());
           if (i<this.list.size())
           {
               line.style("display:flex;text-align:right;width:100%;border-bottom:1px solid #bbb;");
           }
           else
           {
               line.style("display:flex;text-align:right;width:100%;");
           }
           String label=item.getName();
           if (label==null)
           {
               label="";
           }
           line.addInner(new div().style("width:"+width+"em;border-right:1px solid #bbb;padding-right:4px;margin-right:4px;padding-top:0.2em;padding-bottom:0.1em;font-weight:bold;").addInner(label));
           line.addInner(new div().style("width:100%;text-align:left;padding-top:0.2em;padding-bottom:0.1em;").addInner(item.getValue()));
       }
       super.compose(builder);
    }
}
