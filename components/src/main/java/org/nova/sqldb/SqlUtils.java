package org.nova.sqldb;

import org.nova.core.NameObject;
import org.nova.tracing.Trace;

public class SqlUtils
{
    static public String insert(String table,String...values)
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

    static public void insert(Trace parent,String categoryOverride,Accessor accessor,String table,NameObject...nameObjects) throws Throwable
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
        accessor.executeUpdate(parent, categoryOverride, sb.toString(),parameters);
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
        try (Transaction transaction=accessor.beginTransaction(parent,"insertIfNotExist"))
        {
            RowSet rowSet=accessor.executeQuery(parent, categoryOverride, sb.toString(), parameters);
            if (rowSet.size()>0)
            {
                transaction.rollback();
                return;
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
            accessor.executeUpdate(parent, categoryOverride, sb.toString(),parameters);
            transaction.commit();
        }
    }
}
