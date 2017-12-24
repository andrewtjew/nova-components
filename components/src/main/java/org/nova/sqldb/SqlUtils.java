package org.nova.sqldb;

import java.sql.Timestamp;

import org.nova.core.NameObject;
import org.nova.tracing.Trace;

public class SqlUtils
{
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
    static public int insert(Trace parent,Accessor accessor,String table,NameObject...nameObjects) throws Throwable
    {
        return insert(parent,null,accessor,table,nameObjects);
    }
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

    static public int insert(Trace parent,String categoryOverride,Connector connector,String table,NameObject...nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            return insert(parent,categoryOverride,accessor,table,nameObjects);
        }
    }
    
    

    static public void save(Trace parent,String categoryOverride,Connector connector,String table,NameObject keyObject,NameObject activeStatusObject,Object inactiveStatusValue,NameObject...nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            try (Transaction transaction=new Transaction(accessor, parent))
            {
                save(parent,categoryOverride,accessor,table,keyObject,activeStatusObject,inactiveStatusValue,nameObjects);
                transaction.commit();
            }
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

        accessor.executeUpdate(parent,categoryOverride,"UPDATE "+table+" SET "+activeStatusObject.getName()+"=? WHERE "+keyObject.getName()+"=?",
                inactiveStatusValue,keyObject.getValue());
        if (accessor.executeUpdate(parent, categoryOverride, sb.toString(),parameters)!=1)
        {
            throw new Exception("Unable to save");
        }
    }

    static public long saveAndGetLongKey(Trace parent,String categoryOverride,Connector connector,String table,NameObject keyObject,NameObject activeStatusObject,Object inactiveStatusValue,NameObject...nameObjects) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            try (Transaction transaction=new Transaction(accessor, parent))
            {
                long key=saveAndGetLongKey(parent,categoryOverride,accessor,table,keyObject,activeStatusObject,inactiveStatusValue,nameObjects);
                accessor.commit();
                return key;
            }
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
    static public int insertIfNotExist(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject[] keyObjects,NameObject[] additionalObjects) throws Throwable
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
                return 0;
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
            int inserted=accessor.executeUpdate(parent, categoryOverride, sb.toString(),parameters);
            transaction.commit();
            return inserted;
        }
    }
    
    public static RowSet getFirstRowSet(Trace parent,Connector connector,String call,Param...params) throws Throwable
    {
        try (Accessor accessor=connector.openAccessor(parent))
        {
            CallResult<Void> result=accessor.executeCall(parent, null, Void.class, call,params);
            return result.getRowSet(0);
        }
    }
    
}
