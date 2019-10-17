package org.nova.html.deprecated;

import java.util.ArrayList;

import org.nova.core.NameValue;
import org.nova.html.attributes.Color;
import org.nova.html.attributes.Size;
import org.nova.html.attributes.Style;
import org.nova.html.attributes.border_style;
import org.nova.html.attributes.unit;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.ext.Text;
import org.nova.html.tags.div;

public class NameValueList extends div
{
    final private ArrayList<NameValue<Element>> list;
    private int longest;
    final private Size leftWidth;
    final private boolean frame;
    
    public NameValueList(Size leftWidth,boolean frame)
    {
        style("display:block;");
        this.list=new ArrayList<>();
        this.longest=0;
        this.leftWidth=leftWidth;
        this.frame=frame;
    }
    public NameValueList(Size leftWidth)
    {
        this(leftWidth,true);
    }
    public NameValueList()
    {
        this(null,true);
    }
    public NameValueList(boolean frame)
    {
        this(null,frame);
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
           if (this.frame)
           {
               if (i==0)
               {
                   line.style("display:flex;text-align:right;width:100%;border:1px solid #bbb;");
               }
               else if (i%2==0)
               {
                   line.style("display:flex;text-align:right;width:100%;border-bottom:1px solid #bbb;border-left:1px solid #bbb;border-right:1px solid #bbb;");
               }
               else 
               {
                   line.style("display:flex;text-align:right;width:100%;background-color:#f9f9f9;border-bottom:1px solid #bbb;border-left:1px solid #bbb;border-right:1px solid #bbb;");
               }
           }
           else
           {
               line.style("display:flex;text-align:right;");
           }
           String label=item.getName();
           if (label==null)
           {
               label="";
           }
           if (this.frame)
           {
               line.addInner(new div().style
                       (
                           new Style()
                           .width(size)
                           .border_right(new Size(1,unit.px),border_style.solid,Color.rgb(176, 176, 176))
                           .margin_right(new Size(4,unit.px))
                           .padding_right(new Size(4,unit.px))
                           .padding_top(new Size(0,unit.px))
                           .padding(new Size(0.2,unit.em),new Size(4,unit.px),new Size(0.1,unit.em),new Size(0,unit.px))
                       )
                       .addInner(label));
           }
           else
           {
               line.addInner(new div().style
                       (
                           new Style()
                           .width(size)
                           .margin_right(new Size(4,unit.px))
                           .padding_right(new Size(4,unit.px))
                           .padding_top(new Size(0,unit.px))
                           .padding(new Size(0.2,unit.em),new Size(4,unit.px),new Size(0.1,unit.em),new Size(0,unit.px))
                       )
                       .addInner(label));
           }
           line.addInner(new div().style("width:100%;text-align:left;padding-top:0.2em;padding-bottom:0.1em;").addInner(item.getValue()));
       }
       super.compose(builder);
    }
}
