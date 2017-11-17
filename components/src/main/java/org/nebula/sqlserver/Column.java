package org.nebula.sqlserver;

public class Column
{
    private final String name;
    private final String type;
    private final int size;
    private final boolean identity;
    private final long identityStart;
    private final long identityIncrement;
    private final boolean nullAllowed;
    
    public Column(String name,String type,int size,boolean identity,long identityStart,long identityIncrement,boolean nullAllowed)
    {
        this.name=name;
        this.type=type;
        this.size=size;
        this.identity=identity;
        this.identityStart=identityStart;
        this.identityIncrement=identityIncrement;
        this.nullAllowed=nullAllowed;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public int getSize()
    {
        return size;
    }

    public boolean isIdentity()
    {
        return identity;
    }

    public long getIdentityStart()
    {
        return identityStart;
    }
    public long getIdentityIncrement()
    {
        return identityIncrement;
    }
    public boolean isNullAllowed()
    {
        return nullAllowed;
    }
    
    
}
