package org.nova.sqldb;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nova.annotations.Alias;
import org.nova.core.NameObject;
import org.nova.sqldb.FieldMaps.ConstructorFieldMap;
import org.nova.tracing.Trace;

public class SqlUtils
{
    static public Timestamp now()
    {
        return new Timestamp(System.currentTimeMillis());
    }

    static public RowSet executeQuery(Trace parent, String traceCategoryOverride, Connector connector,String sql, Object... parameters) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return accessor.executeQuery(parent, traceCategoryOverride, parameters,sql);
        }
    }

    static public int executeUpdate(Trace parent, String traceCategoryOverride, Connector connector,String sql, Object... parameters) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return accessor.executeUpdate(parent, traceCategoryOverride, parameters,sql);
        }
    }

    static public GeneratedKeys executeUpdateAndReturnGeneratedKeys(Trace parent, String traceCategoryOverride, Connector connector, String sql, Object... parameters) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return accessor.executeUpdateAndReturnGeneratedKeys(parent, traceCategoryOverride, sql, parameters);
        }
    }
    
    public static RowSet executeCallOneRowSet(Trace parent,String categoryOverride,Accessor accessor,String call,Param...params) throws Throwable
    {
        CallResult<Void> result=accessor.executeCall(parent, categoryOverride, Void.class, call,params);
        int length=result.getRowSets().length;
        if (length==0)
        {
            return null;
        }
        if (length>1)
        {
            throw new Exception("Unexpected rowsets:"+length);
        }
        return result.getRowSet(0);
    }
    public static RowSet executeCallOneRowSet(Trace parent,String categoryOverride,Connector connector,String call,Param...params) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return executeCallOneRowSet(parent, categoryOverride, accessor, call, params);
        }
    }
    
    public static Row executeCallOne(Trace parent,String categoryOverride,Accessor accessor,String call,Param...params) throws Throwable
    {
        RowSet rowSet=executeCallOneRowSet(parent, categoryOverride, accessor, call, params);
        if (rowSet==null)
        {
            return null;
        }
        if (rowSet.size()>1)
        {
            throw new Exception("Unexpected row:"+rowSet.size());
        }
        return rowSet.getRow(0);
    }
    public static Row executeCallOne(Trace parent,String categoryOverride,Connector connector,String call,Param...params) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return executeCallOne(parent,categoryOverride,accessor,call,params);
        }
    }


    public static RowSet executeCallOneRowSet(Trace parent,String categoryOverride,Accessor accessor,String call,Object...parameters) throws Throwable
    {
        Param[] params=new Param[parameters.length];
        for (int i=0;i<parameters.length;i++)
        {
            params[i]=Param.In(parameters[i]);
        }
        CallResult<Void> result=accessor.executeCall(parent, categoryOverride, Void.class, call,params);
        int length=result.getRowSets().length;
        if (length==0)
        {
            return null;
        }
        if (length>1)
        {
            throw new Exception("Unexpected rowsets:"+length);
        }
        return result.getRowSet(0);
    }
    public static RowSet executeCallOneRowSet(Trace parent,String categoryOverride,Connector connector,String call,Object...parameters) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return executeCallOneRowSet(parent, categoryOverride, accessor, call, parameters);
        }
    }

    public static Row executeCallOne(Trace parent,String categoryOverride,Accessor accessor,String call,Object parameters) throws Throwable
    {
        RowSet rowSet=executeCallOneRowSet(parent, categoryOverride, accessor, call, parameters);
        if (rowSet==null)
        {
            return null;
        }
        if (rowSet.size()>1)
        {
            throw new Exception("Unexpected row:"+rowSet.size());
        }
        return rowSet.getRow(0);
    }
    public static Row executeCallOne(Trace parent,String categoryOverride,Connector connector,String call,Object...parameters) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return executeCallOne(parent,categoryOverride,accessor,call,parameters);
        }
    }
    
    
    public static Row executeQueryOne(Trace parent, String traceCategoryOverride, Accessor accessor,String sql, Object... parameters) throws Throwable
    {
        RowSet rowSet=accessor.executeQuery(parent, traceCategoryOverride, parameters, sql);
        if (rowSet.size()>1)
        {
            throw new SQLException("Rows returned: "+rowSet.size());
        }
        else if (rowSet.size()==0)
        {
            return null;
        }
        return rowSet.getRow(0);
    }
    public static Row executeQueryOne(Trace parent, String traceCategoryOverride, Connector connector,String sql, Object... parameters) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return executeQueryOne(parent,traceCategoryOverride,accessor,sql,parameters);
        }
    }
    
}
