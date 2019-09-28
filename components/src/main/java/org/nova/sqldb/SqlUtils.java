package org.nova.sqldb;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.nova.annotations.Alias;
import org.nova.core.NameObject;
import org.nova.sqldb.FieldMaps.ConstructorFieldMap;
import org.nova.tracing.Trace;

public class SqlUtils
{
    public static void writeEnumValues(Trace parent,Class<?> type,Accessor accessor) throws Throwable
    {
        String table=type.getSimpleName()+"Values";
        writeEnumValues(parent, type, accessor,table);
    }
    public static void writeEnumValues(Trace parent,Class<?> type,Accessor accessor,String table) throws Throwable
    {
        String sql="if not exists (select * from sysobjects where name='"+table+"' and xtype='U') create table "+table+" ([Value] [smallint] NOT NULL,[Name] [varchar](50) NOT NULL) ON [PRIMARY]";
        accessor.executeUpdate(parent, null, sql);
        for (Object item:type.getEnumConstants())
        {
            Enum<?> constant=(Enum<?>)item;
            String name=constant.name();
            short value=(short)constant.ordinal();
            SqlUtils.insertIfNotExist(parent, null, accessor, table
                    ,new NameObject[]{new NameObject("Value",value),new NameObject("Name",name)}
                    , null);
        }
    }
   
    static public Timestamp now()
    {
        return new Timestamp(System.currentTimeMillis());
    }
    static public int multiAttemptExecuteUpdate(Trace parent,String traceCategoryOverride,int attempts,long betweenRetriesWaitMs,Accessor accessor,String sql,Object...parameters) throws Throwable
    {
        Throwable throwable=null;
        for (int i=0;i<attempts;i++)
        {
            try
            {
                return accessor.executeUpdate(parent, traceCategoryOverride, parameters, sql);
            }
            catch (Throwable t)
            {
                throwable=t;
            }
            Thread.sleep(betweenRetriesWaitMs);
        }
        throw throwable;
    }
    
    
    static public String buildInsert(String table,String...values)
    {
        StringBuilder sb=new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" (");
        sb.append(values[0]);
        for (int i=1;i<values.length;i++)
        {
            sb.append(',').append(values[i]);
        }
        sb.append(") VALUES(");
        sb.append('?');
        for (int i=1;i<values.length;i++)
        {
            sb.append(",?");
        }
        sb.append(')');
        return sb.toString();
    }
    
    static public long insertAndGetLongKey(Trace parent,Accessor accessor,String table,NameObject...nameObjects) throws Throwable
    {
        return insertAndGetLongKey(parent, null,accessor, table, nameObjects);
    }
    static public long insertAndGetLongKey(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject...nameObjects) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" (");
        Object[] parameters=new Object[nameObjects.length];
        sb.append(nameObjects[0].getName());
        parameters[0]=nameObjects[0].getValue();
        for (int i=1;i<nameObjects.length;i++)
        {
            sb.append(',').append(nameObjects[i].getName());
            parameters[i]=nameObjects[i].getValue();
        }
        sb.append(") VALUES(");
        sb.append('?');
        for (int i=1;i<nameObjects.length;i++)
        {
            sb.append(",?");
        }
        sb.append(')');
        return accessor.executeUpdateAndReturnGeneratedKeys(parent, categoryOverride, sb.toString(),parameters).getBigDecimal(0).longValue();
    }
    static public long insertAndGetLongKey(Trace parent,String categoryOverride,Connector connector,String table,NameObject...nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return insertAndGetLongKey(parent, accessor, table, nameObjects);
        }
    }
    static public long insertAndGetLongKey(Trace parent,Connector connector,String table,NameObject...nameObjects) throws Throwable
    {
        return insertAndGetLongKey(parent,null,connector,table,nameObjects);
    }
    static public void insert(Trace parent,Accessor accessor,String table,NameObject...nameObjects) throws Throwable
    {
        insert(parent,null,accessor,table,nameObjects);
    }
    /*
    public static int insert(Trace parent, String traceCategoryOverride, Accessor accessor, String table, Object object) throws Throwable
    {
        Class<?> type=object.getClass();
        if (traceCategoryOverride==null)
        {
            traceCategoryOverride="INSERT INTO TABLE "+table+" OBJECT "+type.getCanonicalName();
        }
        ConstructorFieldMap constructorFieldMap=FieldMaps.get(type);
        HashMap<String,Field> map=constructorFieldMap.map;
        StringBuilder sb=new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" (");
        ArrayList<Object> parameters=new ArrayList<>();
        boolean needComma=false;
        for (Field field:map.values())
        {
            if (needComma)
            {
                sb.append(',');
            }
            else
            {
                needComma=true;
            }
            Alias alias=field.getAnnotation(Alias.class);
            String name=alias!=null?alias.value():field.getName();
            sb.append(name);
            parameters.add(field.get(object));
        }
        sb.append(") VALUES(");
        needComma=false;
        for (int i=0;i<map.size();i++)
        {
            if (needComma)
            {
                sb.append(',');
            }
            else
            {
                needComma=true;
            }
            sb.append('?');
        }
        sb.append(')');
        String sql=sb.toString();
        if (traceCategoryOverride==null)
        {
            traceCategoryOverride=sql;
        }
        return accessor.executeUpdate(parent, traceCategoryOverride, parameters,sql);
    
    }
    */
    static public int insert(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject...nameObjects) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" (");
        Object[] parameters=new Object[nameObjects.length];
        sb.append(nameObjects[0].getName());
        parameters[0]=nameObjects[0].getValue();
        for (int i=1;i<nameObjects.length;i++)
        {
            sb.append(',').append(nameObjects[i].getName());
            parameters[i]=nameObjects[i].getValue();
        }
        sb.append(") VALUES(");
        sb.append('?');
        for (int i=1;i<nameObjects.length;i++)
        {
            sb.append(",?");
        }
        sb.append(')');
        return accessor.executeUpdate(parent, categoryOverride, sb.toString(),parameters);
    }
    static public int updateSingleKeyRow(Trace parent,String categoryOverride,Connector connector,String table,NameObject keyObject,NameObject...nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return updateSingleKeyRow(parent,categoryOverride,accessor,table,keyObject,nameObjects);
        }
    }
    
    
    static public int updateSingleKeyRow(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject keyObject,NameObject...nameObjects) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        sb.append("UPDATE ").append(table).append(" SET ");
        Object[] parameters=new Object[nameObjects.length+1];
        for (int i=0;i<nameObjects.length;i++)
        {
            if (i>0)
            {
                sb.append(",");
            }
            sb.append(nameObjects[i].getName()).append("=?");
            parameters[i]=nameObjects[i].getValue();
        }
        sb.append(" WHERE "+keyObject.getName()+"=?");
        parameters[nameObjects.length]=keyObject.getValue();
        return accessor.executeUpdate(parent, categoryOverride, sb.toString(),parameters);
    }
    static public int[] insertBatch(Trace parent,String categoryOverride,Accessor accessor,String table,Object[][] parameters,String...columns) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" (");
        sb.append(columns[0]);
        for (int i=1;i<columns.length;i++)
        {
            sb.append(',').append(columns[i]);
        }
        sb.append(") VALUES(");
        sb.append('?');
        for (int i=1;i<columns.length;i++)
        {
            sb.append(",?");
        }
        sb.append(')');
        return accessor.executeBatchUpdate(parent, categoryOverride, parameters,sb.toString());
    }

    static public void insert(Trace parent,String categoryOverride,Connector connector,String table,NameObject...nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            insert(parent,categoryOverride,accessor,table,nameObjects);
        }
    }
    
    

    static public void save(Trace parent,String categoryOverride,Connector connector,String table,NameObject keyObject,NameObject activeStatusObject,Object inactiveStatusValue,NameObject...nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            save(parent,categoryOverride,accessor,table,keyObject,activeStatusObject,inactiveStatusValue,nameObjects);
        }
    }

    static public void save(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject keyObject,NameObject activeStatusObject,Object inactiveStatusValue,NameObject...nameObjects) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" (");
        Object[] parameters=new Object[nameObjects.length+2];
        sb.append(keyObject.getName());
        parameters[0]=keyObject.getValue();
        sb.append(',').append(activeStatusObject.getName());
        parameters[1]=activeStatusObject.getValue();
        for (int i=0;i<nameObjects.length;i++)
        {
            sb.append(',').append(nameObjects[i].getName());
            parameters[i+2]=nameObjects[i].getValue();
        }
        sb.append(") VALUES(?,?");
        for (int i=0;i<nameObjects.length;i++)
        {
            sb.append(",?");
        }
        sb.append(')');

        try (Transaction transaction=new Transaction(accessor, parent))
        {
            accessor.executeUpdate(parent,categoryOverride,"UPDATE "+table+" SET "+activeStatusObject.getName()+"=? WHERE "+keyObject.getName()+"=?",
                    inactiveStatusValue,keyObject.getValue());
            if (accessor.executeUpdate(parent, categoryOverride, sb.toString(),parameters)!=1)
            {
                throw new Exception("Unable to save");
            }
            transaction.commit();
        }
    }

    static public long saveAndGetLongKey(Trace parent,String categoryOverride,Connector connector,String table,NameObject keyObject,NameObject activeStatusObject,Object inactiveStatusValue,NameObject...nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return saveAndGetLongKey(parent,categoryOverride,accessor,table,keyObject,activeStatusObject,inactiveStatusValue,nameObjects);
        }
    }

    static public long saveAndGetLongKey(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject keyObject,NameObject activeStatusObject,Object inactiveStatusValue,NameObject...nameObjects) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" (");
        Object[] parameters=new Object[nameObjects.length+2];
        sb.append(keyObject.getName());
        parameters[0]=keyObject.getValue();
        sb.append(',').append(activeStatusObject.getName());
        parameters[1]=activeStatusObject.getValue();
        for (int i=0;i<nameObjects.length;i++)
        {
            sb.append(',').append(nameObjects[i].getName());
            parameters[i+1]=nameObjects[i].getValue();
        }
        sb.append(") VALUES(?,?");
        for (int i=0;i<nameObjects.length;i++)
        {
            sb.append(",?");
        }
        sb.append(')');

        try (Transaction transaction=accessor.beginTransaction("saveAndGetLongKey"))
        {
            accessor.executeUpdate(parent,categoryOverride,"UPDATE "+table+" SET "+activeStatusObject.getName()+"=? WHERE "+keyObject.getName()+"=?",
                    inactiveStatusValue,keyObject.getValue());
            long key=accessor.executeUpdateAndReturnGeneratedKeys(parent, categoryOverride, sb.toString(),parameters).getBigDecimal(0).longValue();
            return key;
        }
    }
    static public void insertIfNotExist(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject[] keyObjects,NameObject[] additionalObjects) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        Object[] parameters=new Object[keyObjects.length];
        sb.append("SELECT * FROM ").append(table).append(" WHERE ");
        sb.append(keyObjects[0].getName()).append("=?");
        parameters[0]=keyObjects[0].getValue();
        for (int i=1;i<keyObjects.length;i++)
        {
            sb.append(" AND ").append(keyObjects[i].getName()).append("=?");
            parameters[i]=keyObjects[i].getValue();
        }
        try (Transaction transaction=accessor.beginTransaction("insertIfNotExist"))
        {
            RowSet rowSet=accessor.executeQuery(parent, categoryOverride, sb.toString(), parameters);
            if (rowSet.size()>0)
            {
                transaction.rollback();
                if (rowSet.size()==1)
                {
                    return;
                }
                throw new Exception("Size="+rowSet.size());
            }

            sb=new StringBuilder();
            int parametersSize=keyObjects.length;
            if (additionalObjects!=null)
            {
                parametersSize+=additionalObjects.length;
            }
            parameters=new Object[parametersSize];
            sb.append("INSERT INTO ").append(table).append(" (");
            sb.append(keyObjects[0].getName());
            parameters[0]=keyObjects[0].getValue();
            int parameterIndex=1;
            for (int i=1;i<keyObjects.length;i++)
            {
                sb.append(',').append(keyObjects[i].getName());
                parameters[parameterIndex++]=keyObjects[i].getValue();
            }
            if (additionalObjects!=null)
            {
                for (int i=0;i<additionalObjects.length;i++)
                {
                    sb.append(',').append(additionalObjects[i].getName());
                    parameters[parameterIndex++]=additionalObjects[i].getValue();
                }
            }
            sb.append(") VALUES(");
            sb.append('?');
            for (int i=1;i<keyObjects.length;i++)
            {
                sb.append(",?");
            }
            if (additionalObjects!=null)
            {
                for (int i=0;i<additionalObjects.length;i++)
                {
                    sb.append(",?");
                }
            }
            sb.append(')');
            accessor.executeUpdate(parent, categoryOverride, sb.toString(),parameters);
            transaction.commit();
        }
    }
    
    static public long insertIfNotExistAndGetLongKey(Trace parent,String categoryOverride,Accessor accessor,String table,String identityColumnName,NameObject[] keyObjects,NameObject[] additionalObjects) throws Throwable
    {
        StringBuilder sb=new StringBuilder();
        Object[] parameters=new Object[keyObjects.length];
        sb.append("SELECT "+identityColumnName+" FROM ").append(table).append(" WHERE ");
        sb.append(keyObjects[0].getName()).append("=?");
        parameters[0]=keyObjects[0].getValue();
        for (int i=1;i<keyObjects.length;i++)
        {
            sb.append(" AND ").append(keyObjects[i].getName()).append("=?");
            parameters[i]=keyObjects[i].getValue();
        }
        long key;
        try (Transaction transaction=accessor.beginTransaction("insertIfNotExist"))
        {
            RowSet rowSet=accessor.executeQuery(parent, categoryOverride, sb.toString(), parameters);
            if (rowSet.size()>0)
            {
                transaction.rollback();
                if (rowSet.size()==1)
                {
                    return rowSet.getRow(0).getBIGINT(0);
                }
                throw new Exception("Size="+rowSet.size());
            }

            sb=new StringBuilder();
            parameters=new Object[keyObjects.length];
            sb.append("INSERT INTO ").append(table).append(" (");
            sb.append(keyObjects[0].getName());
            parameters[0]=keyObjects[0].getValue();
            for (int i=1;i<keyObjects.length;i++)
            {
                sb.append(',').append(keyObjects[i].getName());
                parameters[i]=keyObjects[i].getValue();
            }
            if (additionalObjects!=null)
            {
                for (int i=0;i<additionalObjects.length;i++)
                {
                    sb.append(',').append(additionalObjects[i].getName());
                    parameters[i]=additionalObjects[i].getValue();
                }
            }
            sb.append(") VALUES(");
            sb.append('?');
            for (int i=1;i<keyObjects.length;i++)
            {
                sb.append(",?");
            }
            if (additionalObjects!=null)
            {
                for (int i=0;i<additionalObjects.length;i++)
                {
                    sb.append(",?");
                }
            }
            sb.append(')');
            key=accessor.executeUpdateAndReturnGeneratedKeys(parent, categoryOverride, sb.toString(),parameters).getBigDecimal(0).longValue();
            transaction.commit();
        }
        return key;
    }
    
    static public void updateInsert(Trace parent,String categoryOverride,Connector connector,String table,NameObject[] keyObjects,NameObject[] nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            updateInsert(parent,categoryOverride,accessor,table,keyObjects,nameObjects);
        }
    }
    static public void updateInsert(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject[] keyObjects,NameObject[] nameObjects) throws Throwable
    {
        if (keyObjects.length==0)
        {
            throw new Exception();
        }
        if (nameObjects==null)
        {
            nameObjects=new NameObject[0];
        }
        StringBuilder update=new StringBuilder();
        update.append("UPDATE "+table+" SET ");
        Object[] updateParameters=new Object[nameObjects.length+keyObjects.length];
        for (int i=0;i<nameObjects.length;i++)
        {
            NameObject nameObject=nameObjects[i];
            updateParameters[i]=nameObject.getValue();
            if (i>0)
            {
                update.append(',');
            }
            update.append(nameObject.getName());
            update.append("=?");
        }
        update.append(" WHERE ");
        for (int i=0;i<keyObjects.length;i++)
        {
            if (i>0)
            {
                update.append(" AND ");
            }
            NameObject keyObject=keyObjects[i];
            update.append(keyObject.getName()+"=?");
            updateParameters[nameObjects.length+i]=keyObject.getValue();
        }
        
        try (Transaction transaction=accessor.beginTransaction("updateInsert"))
        {
            if (accessor.executeUpdate(parent, categoryOverride!=null?"update@"+categoryOverride:null, updateParameters, update.toString())==0)
            {
                StringBuilder insert=new StringBuilder();
                insert.append("INSERT INTO ").append(table).append(" (");
                Object[] insertParameters=new Object[keyObjects.length+nameObjects.length];
                for (int i=0;i<keyObjects.length;i++)
                {
                    NameObject keyObject=keyObjects[i];
                    if (i>0)
                    {
                        insert.append(',');
                    }
                    insert.append(keyObject.getName());
                    insertParameters[i]=keyObject.getValue();
                }                
                for (int i=0;i<nameObjects.length;i++)
                {
                    insert.append(',').append(nameObjects[i].getName());
                    insertParameters[i+keyObjects.length]=nameObjects[i].getValue();
                }
                insert.append(") VALUES (");
                for (int i=0;i<keyObjects.length+nameObjects.length;i++)
                {
                    if (i==0)
                    {
                        insert.append("?");
                    }
                    else
                    {
                        insert.append(",?");
                    }
                }
                insert.append(')');

                accessor.executeUpdate(parent, categoryOverride!=null?"insert@"+categoryOverride:null, insertParameters, insert.toString());
            }
            transaction.commit();
        }
    }
    
    static public void insertUpdate(Trace parent,String categoryOverride,Connector connector,String table,NameObject[] keyObjects,NameObject[] nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            insertUpdate(parent,categoryOverride,accessor,table,keyObjects,nameObjects);
        }
    }
    static public void insertUpdate(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject[] keyObjects,NameObject[] nameObjects) throws Throwable
    {
        if (keyObjects.length==0)
        {
            throw new Exception();
        }
        StringBuilder insert=new StringBuilder();
        insert.append("INSERT INTO ").append(table).append(" (");
        Object[] insertParameters=new Object[keyObjects.length+nameObjects.length];
        for (int i=0;i<keyObjects.length;i++)
        {
            NameObject keyObject=keyObjects[i];
            if (i>0)
            {
                insert.append(',');
            }
            insert.append(keyObject.getName());
            insertParameters[i]=keyObject.getValue();
        }                
        for (int i=0;i<nameObjects.length;i++)
        {
            insert.append(',').append(nameObjects[i].getName());
            insertParameters[i+keyObjects.length]=nameObjects[i].getValue();
        }
        insert.append(") VALUES (");
        for (int i=0;i<keyObjects.length+nameObjects.length;i++)
        {
            if (i==0)
            {
                insert.append("?");
            }
            else
            {
                insert.append(",?");
            }
        }
        insert.append(')');

        try (Transaction transaction=accessor.beginTransaction("insertUpdate"))
        {
            try
            {
                accessor.executeUpdate(parent, categoryOverride!=null?"insert@"+categoryOverride:null, insertParameters, insert.toString());
            }
            catch (Throwable t)
            {
                StringBuilder update=new StringBuilder();
                update.append("UPDATE "+table+" SET ");
                Object[] updateParameters=new Object[nameObjects.length+keyObjects.length];
                for (int i=0;i<nameObjects.length;i++)
                {
                    NameObject nameObject=nameObjects[i];
                    if (i>0)
                    {
                        update.append(',');
                    }
                    update.append(nameObject.getName());
                    update.append("=?");
                    updateParameters[i]=nameObject.getValue();
                }
                update.append(" WHERE ");
                for (int i=0;i<keyObjects.length;i++)
                {
                    if (i>0)
                    {
                        update.append(" AND ");
                    }
                    NameObject keyObject=keyObjects[i];
                    update.append(keyObject.getName());
                    update.append("=?");
                    updateParameters[nameObjects.length+i]=keyObject.getValue();
                }
                accessor.executeUpdate(parent, categoryOverride!=null?"update@"+categoryOverride:null, updateParameters, update.toString());
            }
            transaction.commit();
        }
    }
    public static RowSet executeCallOneRowSet(Trace parent,Connector connector,String call,Param...params) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            CallResult<Void> result=accessor.executeCall(parent, null, Void.class, call,params);
            if (result.getRowSets().length>1)
            {
                throw new Exception("Unexpected rowsets:"+result.getRowSets().length);
            }
            if (result.getRowSets().length==0)
            {
                return null;
            }
            return result.getRowSet(0);
        }
    }
    public static Row executeCallOne(Trace parent,Connector connector,String call,Param...params) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            CallResult<Void> result=accessor.executeCall(parent, null, Void.class, call,params);
            if (result.getRowSets().length>1)
            {
                throw new Exception("Unexpected rowsets:"+result.getRowSets().length);
            }
            if (result.getRowSets().length==0)
            {
                return null;
            }
            RowSet rowSet=result.getRowSet(0);
            if (rowSet.size()>1)
            {
                throw new Exception("Unexpected row:"+rowSet.size());
            }
            return rowSet.getRow(0);
        }
    }
    public static RowSet executeCallOneRowSet(Trace parent,Connector connector,String call,Object...inObjects) throws Throwable
    {
        Param[] params=new Param[inObjects.length];
        for (int i=0;i<inObjects.length;i++)
        {
            params[i]=Param.In(inObjects[i]);
        }
        return executeCallOneRowSet(parent,connector,call,params);
    }
    public static Row executeCallOne(Trace parent,Connector connector,String call,Object...inObjects) throws Throwable
    {
        Param[] params=new Param[inObjects.length];
        for (int i=0;i<inObjects.length;i++)
        {
            params[i]=Param.In(inObjects[i]);
        }
        return executeCallOne(parent,connector,call,params);
    }
    
    public static Row executeQueryOne(Trace parent, Accessor accessor,String traceCategoryOverride, String sql, Object... parameters) throws Throwable
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
    public static RowSet executeQuery(Trace parent, Accessor accessor,String traceCategoryOverride, String sql, Object... parameters) throws Throwable
    {
        return accessor.executeQuery(parent, traceCategoryOverride, parameters, sql);
    }
    public static Long executeQueryOneBIGINT(Trace parent, Connector connector,String traceCategoryOverride, String columnName,String sql, Object... parameters) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return executeQueryOneBIGINT(parent,accessor,traceCategoryOverride,columnName,sql,parameters);
        }
    }
    public static Long executeQueryOneBIGINT(Trace parent, Accessor accessor,String traceCategoryOverride, String columnName,String sql, Object... parameters) throws Throwable
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
        return rowSet.getRow(0).getBIGINT(columnName);
    }
    
    public static Row executeQueryOne(Trace parent, Connector connector,String traceCategoryOverride, String sql, Object... parameters) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return executeQueryOne(parent,accessor,traceCategoryOverride,sql,parameters);
        }
    }
    
    public static int selectCount(Trace parent,Accessor accessor,String table,String where,Object...params) throws Throwable
    {
        RowSet rowSet=accessor.executeQuery(parent, null, "SELECT count(*) as Size FROM "+table+" WHERE "+where,params);
        if (rowSet.size()==0)
        {
            return 0;
        }
        return rowSet.getRow(0).getINTEGER(0);
    }
    
}
