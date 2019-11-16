package org.nova.sqldb;

import java.util.ArrayList;

import org.nova.tracing.Trace;

public class Insert 
{
	final private StringBuilder columns;
	final ArrayList<Object> parameters;
	final private String table;
	private String categoryOverride;
	
	public Insert(String table)
	{
		this.columns=new StringBuilder();
		this.parameters=new ArrayList<Object>();
		this.table=table;
	}
	
	public Insert value(String columnName,Object value)
	{
		if (this.parameters.size()>0)
		{
			this.columns.append(',');
		}
		this.columns.append(columnName);
		this.parameters.add(value);
		return this;
	}
	
	public Insert categoryOverride(String categoryOverride)
	{
		this.categoryOverride=categoryOverride;
		return this;
	}
	
	public int execute(Trace parent,Accessor accessor) throws Throwable
	{
		StringBuilder sql=new StringBuilder("INSERT INTO "+this.table+" ("+this.columns.toString()+") VALUES (?");
        for (int i=1;i<this.parameters.size();i++)
        {
            sql.append(",?");
        }
        sql.append(')');
        return accessor.executeUpdate(parent, this.categoryOverride, parameters, sql.toString());
	}

    public long executeAndReturnLongKey(Trace parent,Accessor accessor) throws Throwable
    {
        StringBuilder sql=new StringBuilder("INSERT INTO "+this.table+" ("+this.columns.toString()+") VALUES (?");
        for (int i=1;i<this.parameters.size();i++)
        {
            sql.append(",?");
        }
        sql.append(')');
        return accessor.executeUpdateAndReturnGeneratedKeys(parent, this.categoryOverride, parameters, sql.toString()).getLong(0);
    }
    public int executeAndReturnIntKey(Trace parent,Accessor accessor) throws Throwable
    {
        StringBuilder sql=new StringBuilder("INSERT INTO "+this.table+" ("+this.columns.toString()+") VALUES (?");
        for (int i=1;i<this.parameters.size();i++)
        {
            sql.append(",?");
        }
        sql.append(')');
        return accessor.executeUpdateAndReturnGeneratedKeys(parent, this.categoryOverride, parameters, sql.toString()).getInt(0);
    }
	public int execute(Trace parent,Connector connector) throws Throwable
	{
		try (Accessor accessor=connector.openAccessor(parent))
		{
			return execute(parent, accessor);
		}
	}

	public long executeAndReturnLongKey(Trace parent,Connector connector) throws Throwable
	{
		try (Accessor accessor=connector.openAccessor(parent))
		{
			return executeAndReturnLongKey(parent, accessor);
		}
	}
	
}
