package org.nebula.sqlserver;

public class Procedure
{
    final private String owner;
    final private String name;
    final private String body;
    final private String text;
    Procedure(String text,String owner,String name,String body)
    {
        this.text=text;
        this.owner=owner;
        this.name=name;
        this.body=body;
    }
    public String getText()
    {
        return this.text;
    }
    public String getOwner()
    {
        return owner;
    }
    public String getName()
    {
        return name;
    }
    public String getBody()
    {
        return body;
    }
    
}
