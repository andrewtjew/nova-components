package org.nova.html.widgets;

import java.util.ArrayList;

import org.nova.core.NameValue;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.div;
import org.nova.html.tags.span;

public class NameInputValueList extends div
{
    final private ArrayList<NameValue<Element>> list;
    private int longest;

    public NameInputValueList()
    {
        style("display:block;");
        this.list=new ArrayList<>();
        this.longest=0;
    }
    public NameInputValueList add(String name,Element element)
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
    public NameInputValueList add(String name,Object value)
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
       int width=(int)((this.longest+2)*0.6);
       for (int i=0;i<this.list.size();i++)
       {
           NameValue<Element> item=this.list.get(i);

           div line=returnAddInner(new div().style("display:flex;text-align:right;width:100%;margin:0.25em;"));
           String label=item.getName();
           if (label==null)
           {
               label="";
           }
           else
           {
               label+=":";
           }
           line.addInner(new div().style("width:"+width+"em;padding-right:0.5em;margin-left:0.5em;font-weight:bold;").addInner(label));
//           line.addInner(new div().style("width:100%;text-align:left;display:block;").addInner(item.getValue()));
           line.addInner(new div().style("width:100%;text-align:left;display:block;").addInner(item.getValue()).addInner(new div().style("display:none;padding:0.5em;margin-bottom:0.5em;background-color:#fdd;")));
       }
       super.compose(builder);
    }
    public int size()
    {
        return this.list.size();
    }
}
