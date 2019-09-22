package org.nova.sqldb;

import org.nova.collections.ContentCache;
import org.nova.json.ObjectMapper;
import org.nova.tracing.Trace;

public class KeyValueStore extends ContentCache<String,String>
{
    final private Connector connector;
    final private String tableName;
    final private String insert;
    final private String select;
    final private String update;
    final private int maxKeyLength;
    final private int maxValueLength;
    final private String nameSpace;
    
    
    public KeyValueStore(Connector connector,String tableName,int maxKeyLength,int maxValueLength,String nameSpace)
    {
        this.connector=connector;
        this.tableName=tableName;
        this.insert="INSERT INTO "+tableName+" (k,v) VALUES (?,?)";
        this.select="SELECT v FROM "+tableName+" WHERE k=?";
        this.update="UPDATE "+tableName+" SET v=? WHERE k=?";
        this.maxKeyLength=maxKeyLength;
        this.maxValueLength=maxValueLength;
        this.nameSpace=nameSpace;
    }
    public KeyValueStore(Connector connector,String tableName,String nameSpace)
    {
        this(connector,tableName,50,8000,nameSpace);
    }

    public KeyValueStore(Connector connector,String tableName)
    {
        this(connector,tableName,null);
    }
    
    private String getFullKey(String key)
    {
        if (this.nameSpace==null)
        {
            return key;
        }
        return this.nameSpace+"."+key;
    }
    
    public void write(Trace parent,String key,String value) throws Throwable
    {
        try (Accessor accessor=this.connector.openAccessor(parent))
        {
            write(parent,accessor,key,value);
        }
    }    

    private void write(Trace parent,Accessor accessor,String key,String value) throws Throwable
    {
        String fullKey=getFullKey(key);
        int updated=0;
        if ((fullKey!=null)&&(fullKey.length()>this.maxKeyLength))
        {
            throw new Exception("fullKey length exceeds maximum. maxKeyLength="+this.maxKeyLength+", fullKey length="+fullKey.length());
            
        }
        if ((value!=null)&&(value.length()>this.maxValueLength))
        {
            throw new Exception("value length exceeds maximum. maxValueLength="+this.maxValueLength+", value length="+value.length());
        }
        updated=accessor.executeUpdate(parent, null, update,value,fullKey);
        if (updated==0)
        {
            updated=accessor.executeUpdate(parent, null, insert, fullKey,value);
        }
        if (updated!=1)
        {
            throw new Exception("fullKey="+fullKey); 
        }
        put(parent,key,new ValueSize<String>(value));
    }    

    public void write(Trace parent,String key,Object value) throws Throwable
    {
        write(parent,key,ObjectMapper.writeObjectToString(value));
    }    

    public void write(Trace parent,Object value) throws Throwable
    {
        write(parent,value.getClass().getSimpleName(),ObjectMapper.writeObjectToString(value));
    }    

    public String read(Trace parent,String key) throws Throwable
    {
        return this.get(parent,key);
    }
    
    public String read(Trace parent,String key,String defaultValue) throws Throwable
    {
        String value=read(parent,key);
        if (value!=null)
        {
            return value; 
        }
        return defaultValue;
    }    
    public Double readDouble(Trace parent,String key) throws Throwable
    {
        String value=read(parent,key);
        if (value!=null)
        {
            return Double.parseDouble(value); 
        }
        return null;
    }    
    public double readDouble(Trace parent,String key,double defaultValue) throws Throwable
    {
        Double value=readDouble(parent,key);
        if (value!=null)
        {
            return value; 
        }
        return defaultValue;
    }    
    public Long readLong(Trace parent,String key) throws Throwable
    {
        String value=read(parent,key);
        if (value!=null)
        {
            return Long.parseLong(value); 
        }
        return null;
    }    
    public long readLong(Trace parent,String key,long defaultValue) throws Throwable
    {
        Long value=readLong(parent,key);
        if (value!=null)
        {
            return value; 
        }
        return defaultValue;
    }    
    public Integer readInteger(Trace parent,String key) throws Throwable
    {
        String value=read(parent,key);
        if (value!=null)
        {
            return Integer.parseInt(value); 
        }
        return null;
    }    
    public int readInteger(Trace parent,String key,int defaultValue) throws Throwable
    {
        Integer value=readInteger(parent,key);
        if (value!=null)
        {
            return value; 
        }
        return defaultValue;
    }    

    public <OBJECT> OBJECT read(Trace parent,String key,Class<OBJECT> type) throws Throwable
    {
        return ObjectMapper.readObject(read(parent,key), type);
    }    

    public <OBJECT> OBJECT read(Trace parent,Class<OBJECT> type) throws Throwable
    {
        return read(parent,type.getSimpleName(), type);
    }    

    public <OBJECT> OBJECT read(Trace parent,String key,Class<OBJECT> type,OBJECT defaultValue) throws Throwable
    {
        OBJECT value=ObjectMapper.readObject(read(parent,key), type);
        if (value!=null)
        {
            return value; 
        }
        return defaultValue;
    }
    @Override
    protected ValueSize<String> load(Trace parent, String key) throws Throwable
    {
        String fullKey=getFullKey(key);
        Row row=SqlUtils.executeQueryOne(parent, this.connector, null, this.select, fullKey);
        if (row==null)
        {
            return new ValueSize(null);
        }
        String value=row.getVARCHAR(0);
        return new ValueSize(value);
    }    

}
