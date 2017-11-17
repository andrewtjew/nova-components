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
    final private int maxKeyLength;
    final private int maxValueLength;
    
    public KeyValueStore(Connector connector,String tableName,int maxKeyLength,int maxValueLength)
    {
        this.connector=connector;
        this.tableName=tableName;
        this.insert="INSERT INTO "+tableName+" (k,v) VALUES (?,?)";
        this.select="SELECT v FROM "+tableName+" WHERE k=?";
        this.update="UPDATE "+tableName+" SET v=? WHERE k=?";
        this.maxKeyLength=maxKeyLength;
        this.maxValueLength=maxValueLength;
    }
    public KeyValueStore(Connector connector,String tableName)
    {
        this(connector,tableName,50,8000);
    }
    
    public void write(Trace parent,String key,String value) throws Throwable
    {
        try (Accessor accessor=this.connector.openAccessor(parent, "KeyValueStore"))
        {
            write(parent,accessor,key,value);
        }
    }    

    private void write(Trace parent,Accessor accessor,String key,String value) throws Throwable
    {
        int updated=0;
        if ((key!=null)&&(key.length()>this.maxKeyLength))
        {
            throw new Exception("key length exceeds maximum. maxKeyLength="+this.maxKeyLength+", key length="+key.length());
            
        }
        if ((value!=null)&&(value.length()>this.maxValueLength))
        {
            throw new Exception("value length exceeds maximum. maxValueLength="+this.maxValueLength+", value length="+value.length());
        }
        updated=accessor.executeUpdate(parent, null, update,value,key);
        if (updated==0)
        {
            updated=accessor.executeUpdate(parent, null, insert, key,value);
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
        try (Accessor accessor=this.connector.openAccessor(parent, "KeyValueStore.read"))
        {
            RowSet rowSet=accessor.executeQuery(parent, null, select,key);
            if (rowSet.size()==1)
            {
                return rowSet.getRow(0).getVARCHAR(0);
            }
            return null;
        }
    }    

    public <OBJECT> OBJECT read(Trace parent,String key,Class<OBJECT> type) throws Throwable
    {
        return ObjectMapper.read(read(parent,key), type);
    }    

    public <OBJECT> OBJECT read(Trace parent,Class<OBJECT> type) throws Throwable
    {
        return read(parent,type.getSimpleName(), type);
    }    
    
}
