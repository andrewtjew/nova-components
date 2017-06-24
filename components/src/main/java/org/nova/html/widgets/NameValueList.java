package org.nova.html.widgets;

import java.util.ArrayList;

import org.nova.core.NameValue;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.properties.BorderStyleRect;
import org.nova.html.properties.Color;
import org.nova.html.properties.Size;
import org.nova.html.properties.Style;
import org.nova.html.properties.border_style;
import org.nova.html.properties.font_weight;
import org.nova.html.properties.unit;
import org.nova.html.tags.div;
import org.nova.html.tags.span;

public class NameValueList extends div
{
    final private ArrayList<NameValue<Element>> list;
    private int longest;
    final private Size leftWidth;
    
    public NameValueList(Size leftWidth)
    {
        style("display:block;");
        this.list=new ArrayList<>();
        this.longest=0;
        this.leftWidth=leftWidth;
    }
    public NameValueList()
    {
        this(null);
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
       Size size=this.leftWidth!=null?this.leftWidth:new Size(width,unit.em);
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
           line.addInner(new div().style
                   (
                       new Style()
                       .width(size)
                       .border_right(new Size(1,unit.px),border_style.solid,Color.rgb(176, 176, 176))
                       .margin_right(new Size(4,unit.px))
                       .padding_right(new Size(4,unit.px))
                       .padding_top(new Size(0,unit.px))
                       .padding(new Size(0.2,unit.em),new Size(4,unit.px),new Size(0.1,unit.em),new Size(0,unit.px))
                       .font_weight(font_weight.bold)
                   )
                   .addInner(label));
           line.addInner(new div().style("width:100%;text-align:left;padding-top:0.2em;padding-bottom:0.1em;").addInner(item.getValue()));
       }
       super.compose(builder);
    }
}