package org.nebula.sqlserver;

public class Table
{
    final private String owner;
    final private String name;
    final private Column[] columns;
    final private String text;
    public Table(String text,String owner,String name,Column[] columns)
    {
        this.owner=owner;
        this.name=name;
        this.columns=columns;
        this.text=text;
    }
    public String getOwner()
    {
        return this.owner;
    }
    public String getName()
    {
        return name;
    }
    public Column[] getColumns()
    {
        return columns;
    }
    public String getText()
    {
        return this.text; 
    }
    
}
