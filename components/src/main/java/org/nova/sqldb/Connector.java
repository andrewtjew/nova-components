package org.nova.sqldb;

import java.sql.Connection;
import java.util.List;

import org.nova.collections.Pool;
import org.nova.logging.Logger;
import org.nova.metrics.CountMeter;
import org.nova.metrics.RateMeter;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

import com.nova.disrupt.Disruptor;

public abstract class Connector
{
	final protected TraceManager traceManager;
    final protected Pool<Accessor> pool;
    final protected Disruptor disruptor;
    final Logger logger;
	final CountMeter closeConnectionExceptions; 
	final CountMeter createConnectionExceptions; 
	final CountMeter initialConnectionExceptions; 
	final CountMeter openConnectionSuccesses; 
    final RateMeter rowsQueriedRate;
    final RateMeter rowsUpdatedRate; 
    final RateMeter queryRate;
    final RateMeter updateRate; 
    final RateMeter beginTransactionRate;
    final RateMeter commitTransactionRate;
    final RateMeter rollbackTransactionRate;

    final CountMeter commitFailures;
    final CountMeter rollbackFailures;
    final CountMeter executeFailures;
    final RateMeter callRate;
	
	public Connector(TraceManager traceManager,Logger logger,Disruptor disruptor,long maximumRecentlyUsedCount)
	{
		this.traceManager=traceManager;
		this.pool=new Pool<Accessor>(traceManager,maximumRecentlyUsedCount);
		this.closeConnectionExceptions=new CountMeter();
		this.createConnectionExceptions=new CountMeter();
		this.initialConnectionExceptions=new CountMeter();
		this.openConnectionSuccesses=new CountMeter();
		this.logger=logger;
        this.rowsQueriedRate=new RateMeter();
        this.rowsUpdatedRate=new RateMeter();
        this.queryRate=new RateMeter();
        this.updateRate=new RateMeter();
        this.beginTransactionRate=new RateMeter();
        this.commitTransactionRate=new RateMeter();
        this.rollbackTransactionRate=new RateMeter();
        this.callRate=new RateMeter();
        this.commitFailures=new CountMeter();
        this.rollbackFailures=new CountMeter();
        this.executeFailures=new CountMeter();
		this.disruptor=disruptor;
	}
	abstract protected Connection createConnection() throws Throwable;
	abstract public String getName();
	
	public Accessor openAccessor(Trace parent,String traceCategoryOverride,long timeout) throws Throwable
	{
		if (traceCategoryOverride==null)
		{
			traceCategoryOverride="Connector."+getName()+".openAccessor";
		}
		return this.pool.waitForAvailable(parent, traceCategoryOverride, timeout);
	}
	public Accessor openAccessor(Trace parent,String traceCategoryOverride) throws Throwable
	{
		if (traceCategoryOverride==null)
		{
            traceCategoryOverride="Connector."+getName()+".openAccessor";
		}
		return this.pool.waitForAvailable(parent, traceCategoryOverride);
	}
	
    public RowSet executeQuery(Trace parent, String traceCategoryOverride, String sql, Object... parameters) throws Throwable
    {
        return executeQuery(parent, traceCategoryOverride, parameters, sql);
    }
    public RowSet executeQuery(Trace parent, String traceCategoryOverride, Object[] parameters,String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeQuery"))
        {
            return accessor.executeQuery(parent, traceCategoryOverride,  parameters,sql);
        }
    }
    public RowSet executeQuery(Trace parent, String traceCategoryOverride, List<Object> parameters,String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeQuery"))
        {
            return accessor.executeQuery(parent, traceCategoryOverride,  parameters,sql);
        }
    }

    public int executeUpdate(Trace parent, String traceCategoryOverride, String sql, Object... parameters) throws Throwable
	{
		return executeUpdate(parent, traceCategoryOverride, parameters, sql);
	}
    public int executeUpdate(Trace parent, String traceCategoryOverride, Object[] parameters,String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeUpdate"))
        {
            return accessor.executeUpdate(parent, traceCategoryOverride, parameters,sql);
        }
    }
    public int executeUpdate(Trace parent, String traceCategoryOverride, List<Object> parameters,String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeUpdate"))
        {
            return accessor.executeUpdate(parent, traceCategoryOverride, parameters,sql);
        }
    }

    public GeneratedKeys executeUpdateAndReturnGeneratedKeys(Trace parent, String traceCategoryOverride, String sql, Object... parameters) throws Throwable
    {
        return executeUpdateAndReturnGeneratedKeys(parent, traceCategoryOverride, parameters, sql);
    }
    public GeneratedKeys executeUpdateAndReturnGeneratedKeys(Trace parent, String traceCategoryOverride, Object[] parameters,String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeUpdate"))
        {
            return accessor.executeUpdateAndReturnGeneratedKeys(parent, traceCategoryOverride, parameters,sql);
        }
    }
    public GeneratedKeys executeUpdateAndReturnGeneratedKeys(Trace parent, String traceCategoryOverride, List<Object> parameters,String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeUpdate"))
        {
            return accessor.executeUpdateAndReturnGeneratedKeys(parent, traceCategoryOverride, parameters,sql);
        }
    }
    
    public int[] executeBatchUpdate(Trace parent, String traceCategoryOverride, Object[][] parameters,String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeUpdate"))
        {
            return accessor.executeBatchUpdate(parent, traceCategoryOverride, parameters,sql);
        }
    }
    public int[] executeUpdateBatch(Trace parent, String traceCategoryOverride, List<List<Object>> parameters,String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeUpdate"))
        {
            return accessor.executeBatchUpdate(parent, traceCategoryOverride, parameters,sql);
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
    public <TYPE> TYPE[] executeQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, List<Object> parameters, String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeQuery"))
        {
            return accessor.executeQuery(parent,traceCategoryOverride,type,parameters,sql);
        }
    }
    public <TYPE> TYPE[] executeQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, Object[] parameters, String sql) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeQuery"))
        {
            return accessor.executeQuery(parent,traceCategoryOverride,type,parameters,sql);
        }
    }

	
    public <RETURN_TYPE> CallResult<RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<RETURN_TYPE> returnType,Param[] parameters,String name) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeCall"))
        {
            return accessor.executeCall(parent, traceCategoryOverride, returnType, parameters, name);
        }
    }
    public <RETURN_TYPE> CallResult<RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<RETURN_TYPE> returnType,List<Param> parameters,String name) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeCall"))
        {
            return accessor.executeCall(parent, traceCategoryOverride, returnType, parameters, name);
        }
    }
    public <RETURN_TYPE> CallResult<RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<RETURN_TYPE> returnType,String name,Param...parameters) throws Throwable
    {
        try (Accessor accessor=openAccessor(parent, "Connector."+getName()+".executeCall"))
        {
            return accessor.executeCall(parent, traceCategoryOverride, returnType, name, parameters);
        }
    }
}
