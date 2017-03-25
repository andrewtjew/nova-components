package org.nova.html.pages;

import java.util.ArrayList;

import org.nova.core.KeyValue;

public class TableList
{
    static class Value
    {
        final Object value;
        final Attribute[] attributes;
        Value(Object value,Attribute[] attributes)
        {
            this.value=value;
            this.attributes=attributes;
        }
    }
    final private String seperator;
    private Attribute[] nameAttributes=new Attribute[0];
    private Attribute[] valueAttributes=new Attribute[0];
    private Attribute[] nameHeadingAttributes=new Attribute[0];
    private Attribute[] valueHeadingAttributes=new Attribute[0];
    private HtmlWriter writer;

    public TableList(String seperator,Attribute...attributes)
    {
        this.seperator=seperator;
        this.writer=new HtmlWriter();
        this.writer.begin_table(attributes);
    }
    public TableList(Attribute...attributes)
    {
        this(":",attributes);
    }
    
    public TableList heading(String name,Object value,Attribute...rowAttributes)
    {
        writer.begin_tr(rowAttributes);
        writer.th(name,nameHeadingAttributes);
        if (this.seperator!=null)
        {
            writer.td(this.seperator);
        }
        writer.th(value,this.valueHeadingAttributes);
        writer.end_tr();
        return this;
    }
    
    public TableList row(String name,Object value,Attribute...rowAttributes)
    {
        writer.begin_tr(rowAttributes);
        writer.td(name,nameAttributes);
        if (this.seperator!=null)
        {
            writer.td(this.seperator);
        }
        writer.td(value,this.valueAttributes);
        writer.end_tr();
        return this;
    }
    
    public TableList nameAttributes(Attribute...attributes)
    {
        this.nameAttributes=attributes;
        return this;
    }
    
    public TableList valueAttributes(Attribute...attributes)
    {
        this.valueAttributes=attributes;
        return this;
    }
    
    public TableList nameHeadingAttributes(Attribute...attributes)
    {
        this.nameHeadingAttributes=attributes;
        return this;
    }
    
    public TableList valueHeadingAttributes(Attribute...attributes)
    {
        this.valueHeadingAttributes=attributes;
        return this;
    }
    
    
    public String toString()
    {
        writer.end_table();
        String result=writer.toString();
        writer=null;
        return result;
        
    }
}
