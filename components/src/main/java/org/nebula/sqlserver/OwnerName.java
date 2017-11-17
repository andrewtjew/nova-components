package org.nebula.sqlserver;

public class OwnerName
{
    final private String owner;
    final private String name;
    OwnerName(String owner,String name)
    {
        this.owner=owner;
        this.name=name;
    }
    public String getOwner()
    {
        return owner;
    }
    public String getName()
    {
        return name;
    }
    
}
