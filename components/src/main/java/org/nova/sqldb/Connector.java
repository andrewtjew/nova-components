package org.nova.sqldb;

import java.sql.Connection;

import org.nova.collections.Pool;
import org.nova.metrics.CountMeter;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public abstract class Connector
{
	final protected TraceManager traceManager;
	final protected Pool<Accessor> pool;
	
	final CountMeter closeConnectionExceptions; 
	final CountMeter createConnectionExceptions; 
	final CountMeter initialConnectionExceptions; 
	final CountMeter openConnectionSuccesses; 
	
	public Connector(TraceManager traceManager,long maximumRecentlyUsedCount)
	{
		this.traceManager=traceManager;
		this.pool=new Pool<Accessor>(traceManager,maximumRecentlyUsedCount);
		this.closeConnectionExceptions=new CountMeter();
		this.createConnectionExceptions=new CountMeter();
		this.initialConnectionExceptions=new CountMeter();
		this.openConnectionSuccesses=new CountMeter();
	}

	abstract protected Connection createConnection() throws Throwable;
	abstract public String getName() throws Throwable;
	
	public Accessor openAccessor(Trace parent,String traceCategoryOverride,long timeout) throws Throwable
	{
		if (traceCategoryOverride==null)
		{
			traceCategoryOverride="Connector.Open."+getName();
		}
		return this.pool.waitForAvailable(parent, traceCategoryOverride, timeout);
	}
	public Accessor openAccessor(Trace parent,String traceCategoryOverride) throws Throwable
	{
		if (traceCategoryOverride==null)
		{
			traceCategoryOverride="Connector.Open."+getName();
		}
		return this.pool.waitForAvailable(parent, traceCategoryOverride);
	}
	
	public int executeUpdate(Trace parent, String traceCategoryOverride, String sql, Object... parameters) throws Throwable
	{
		return executeUpdate(parent, traceCategoryOverride, sql, parameters);
	}
	public int executeUpdate(Trace parent, String traceCategoryOverride, Object[] parameters,String sql) throws Throwable
	{
		try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeUpdate"))
		{
			return accessor.executeUpdate(parent, traceCategoryOverride, parameters,sql);
		}
	}
	
	public <TYPE> TYPE executeQuerySingle(Trace parent, String traceCategoryOverride, Class<TYPE> type, String sql,Object...parameters) throws Throwable
	{
		return executeQuerySingle(parent,traceCategoryOverride,type,parameters,sql);
	}
	public <TYPE> TYPE executeQuerySingle(Trace parent, String traceCategoryOverride, Class<TYPE> type, Object[] parameters, String sql) throws Throwable
	{
		try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeQuerySingle"))
		{
			return accessor.executeQuerySingle(parent,traceCategoryOverride,type,parameters,sql);
		}
	}
	public <TYPE> TYPE[] executeQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, String sql, Object... parameters) throws Throwable
	{
		return executeQuery(parent,traceCategoryOverride,type,parameters,sql);
	}

	public <TYPE> TYPE[] executeQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, Object[] parameters, String sql) throws Throwable
	{
		try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeQuery"))
		{
			return accessor.executeQuery(parent,traceCategoryOverride,type,parameters,sql);
		}
	}

	public <RETURN_TYPE> CallRowSetResult<RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<RETURN_TYPE> returnType,String name,Param...parameters) throws Throwable
	{
		try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeCall"))
		{
			return accessor.executeCall(parent, traceCategoryOverride, returnType, name, parameters);
		}
	}

	
	
}
