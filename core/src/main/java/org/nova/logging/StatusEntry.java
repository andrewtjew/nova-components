package org.nova.logging;

public class StatusEntry
{
    final private String name;
    final private String source;
    final private String value;
    final private long createdMs;
    final private int count;
    
    StatusEntry(String name,String source,String value,int count)
    {
        this.name=name;
        this.source=source;
        this.value=value;
        this.createdMs=System.currentTimeMillis();
        this.count=count;
    }
    public String getName()
    {
        return name;
    }
    public int getCount()
    {
        return count;
    }
    public String getSource()
    {
        return source;
    }

    public String getValue()
    {
        return value;
    }

    public long getCreatedMs()
    {
        return createdMs;
    }
    
}
