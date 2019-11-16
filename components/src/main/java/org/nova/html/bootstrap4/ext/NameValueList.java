package org.nova.html.bootstrap4.ext;

import org.nova.html.attributes.Size;
import org.nova.html.attributes.Style;
import org.nova.html.attributes.text_align;
import org.nova.html.bootstrap4.Col;
import org.nova.html.bootstrap4.Component;
import org.nova.html.bootstrap4.Container;
import org.nova.html.bootstrap4.FormLabel;
import org.nova.html.bootstrap4.Item;
import org.nova.html.bootstrap4.Row;
import org.nova.html.bootstrap4.StyleComponent;
import org.nova.html.bootstrap4.Table;
import org.nova.html.bootstrap4.TableRow;
import org.nova.html.bootstrap4.classes.DeviceClass;
import org.nova.html.bootstrap4.classes.Edge;
import org.nova.html.bootstrap4.classes.Float_;
import org.nova.html.bootstrap4.classes.Font;
import org.nova.html.bootstrap4.classes.Justify;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.bootstrap4.classes.TextAlign;
import org.nova.html.elements.Element;
import org.nova.html.tags.b;
import org.nova.html.tags.hr;
import org.nova.html.tags.td;


public class NameValueList extends StyleComponent<NameValueList>
{
    final private Size width;

    public NameValueList(Size width)
    {
        super("div",null);
        this.width=width;
    }

    public NameValueList add(Object name, Element element)
    {
        Item item=returnAddInner(new Item());
        item.d_flex().justify_content(Justify.start);
        item.addInner(new Item().mr(2).text(TextAlign.right).addInner(new b().addInner(name)).style(new Style().width(this.width)));
        item.addInner(element);
        return this;
    }
    public NameValueList add(Object name, Object value)
    {
        Item item=returnAddInner(new Item());
        item.d_flex().justify_content(Justify.start);
        item.addInner(new Item().mr(2).text(TextAlign.right).addInner(new b().addInner(name)).style(new Style().width(this.width)));
        item.addInner(new Item().addInner(value));
        
        return this;
    }
    public NameValueList add(Integer topSpacing,Integer bottomSpacing,Object name, Object value)
    {
        Item item=returnAddInner(new Item());
        item.d_flex().justify_content(Justify.start);
        item.addInner(new Item().mr(2).text(TextAlign.right).addInner(new b().addInner(name)).style(new Style().width(this.width)));
        item.addInner(new Item().addInner(value));
        if (topSpacing!=null)
        {
            item.mt(topSpacing);
        }
        if (bottomSpacing!=null)
        {
            item.mb(bottomSpacing);
        }
        return this;
    }
    public NameValueList add(Integer topSpacing,Integer bottomSpacing,Object name, Element element)
    {
        Item item=returnAddInner(new Item());
        item.d_flex().justify_content(Justify.start);
        item.addInner(new Item().mr(2).text(TextAlign.right).addInner(new b().addInner(name)).style(new Style().width(this.width)));
        item.addInner(element);
        if (topSpacing!=null)
        {
            item.mt(topSpacing);
        }
        if (bottomSpacing!=null)
        {
            item.mb(bottomSpacing);
        }
        return this;
    }

}
