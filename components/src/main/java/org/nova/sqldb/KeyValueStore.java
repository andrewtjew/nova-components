package org.nova.sqldb;

import org.nova.json.ObjectMapper;
import org.nova.tracing.Trace;

public class KeyValueStore
{
    final private Connector connector;
    final private String tableName;
    final private String insert;
    final private String select;
    final private String update;
    
    public KeyValueStore(Connector connector,String tableName)
    {
        this.connector=connector;
        this.tableName=tableName;
        this.insert="INSERT INTO "+tableName+" (k,v) VALUES (?,?)";
        this.select="SELECT v FROM "+tableName+" WHERE k=?";
        this.update="UPDATE "+tableName+" SET v=? WHERE k=?";
    }
    
    public void write(Trace parent,String key,String value) throws Throwable
    {
        try (Accessor accessor=this.connector.openAccessor(parent, "KeyValueStore"))
        {
            write(parent,accessor,key,value);
        }
    }    

    public void write(Trace parent,Accessor accessor,String key,String value) throws Throwable
    {
        int updated=0;
        try
        {
            updated=accessor.executeUpdate(parent, null, insert, key,value);
        }
        catch (Throwable t)
        {
        }
        if (updated!=1)
        {
            updated=accessor.executeUpdate(parent, null, update,value,key);
        }
        if (updated!=1)
        {
            throw new Exception("key="+key); 
        }
    }    

    public void write(Trace parent,String key,Object value) throws Throwable
    {
        write(parent,key,ObjectMapper.write(value));
    }    

    public void write(Trace parent,Object value) throws Throwable
    {
        write(parent,value.getClass().getSimpleName(),ObjectMapper.write(value));
    }    

    public String read(Trace parent,String key) throws Throwable
    {
        try (Accessor accessor=this.connector.openAccessor(parent, "KeyValueStore"))
        {
            return read(parent,accessor,key);
        }
    }    

    public String read(Trace parent,Accessor accessor,String key) throws Throwable
    {
        RowSet rowSet=accessor.executeQuery(parent, null, select,key);
        if (rowSet.size()==1)
        {
            return rowSet.getRow(0).getVARCHAR(0);
        }
        return null;
    }    
    
    public <OBJECT> OBJECT read(Trace parent,Accessor accessor,String key,Class<OBJECT> type) throws Throwable
    {
        return ObjectMapper.read(read(parent,accessor,key), type);
    }    
    public <OBJECT> OBJECT read(Trace parent,Accessor accessor,Class<OBJECT> type) throws Throwable
    {
        return ObjectMapper.read(read(parent,accessor,type.getSimpleName()), type);
    }    
    
}
