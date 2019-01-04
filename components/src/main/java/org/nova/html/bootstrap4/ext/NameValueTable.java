package org.nova.html.bootstrap4.ext;


import org.nova.html.attributes.Style;
import org.nova.html.attributes.text_align;
import org.nova.html.bootstrap4.Table;
import org.nova.html.bootstrap4.TableRow;
import org.nova.html.bootstrap4.classes.Float_;
import org.nova.html.bootstrap4.classes.Font;
import org.nova.html.tags.td;

public class NameValueTable extends Table
{
    public NameValueTable add(Object name,Object value)
    {
        TableRow tr=new TableRow();
        addRow(tr);
        tr.add(new Span().font(Font.weight_bold).float_(Float_.right).addInner(name),value);
        return this;
    }
    public NameValueTable add(Object name,Object value,text_align valueAlign)
    {
        TableRow tr=new TableRow();
        addRow(tr);
        tr.add(new Span().font(Font.weight_bold).float_(Float_.right).addInner(name),new td().style(new Style().text_align(valueAlign)).addInner(value));
        return this;
    }
    public NameValueTable add(Object name,Object value,Style valueStyle)
    {
        TableRow tr=new TableRow();
        addRow(tr);
        tr.add(new Span().font(Font.weight_bold).float_(Float_.right).addInner(name),new td().style(valueStyle).addInner(value));
        return this;
    }
    public NameValueTable add(Object name,Object value,String valueStyle)
    {
        TableRow tr=new TableRow();
        addRow(tr);
        tr.add(new Span().font(Font.weight_bold).float_(Float_.right).addInner(name),new td().style(valueStyle).addInner(value));
        return this;
    }
}
