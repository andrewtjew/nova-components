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
    
    static public long insertAndGetLongKey(Trace parent,Accessor accessor,String table,NameObject...nameObjects) throws Throwable
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
        return accessor.executeUpdateAndReturnGeneratedKeys(parent, null, sb.toString(),parameters).getBigDecimal(0).longValue();
    }

    static public void insert(Trace parent,Accessor accessor,String table,NameObject...nameObjects) throws Throwable
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
        accessor.executeUpdate(parent, null, sb.toString(),parameters);
    }
}
