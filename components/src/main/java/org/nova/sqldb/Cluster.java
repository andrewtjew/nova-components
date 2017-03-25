package org.nova.sqldb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nova.concurrent.Future;
import org.nova.concurrent.FutureScheduler;
import org.nova.core.Callable;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceCallable;

public class Cluster
{
	final private ClusterNode[] nodes;
	final private FutureScheduler scheduler;
	
	private static int hash(String key)
	{
		int hash = 7;
		for (int i=0;i<key.length();i++) 
		{
		    hash=hash*31+key.charAt(i);
		}
		return hash;
	}

	public Cluster(FutureScheduler scheduler,ClusterNode[] nodes)
	{
		this.nodes=nodes;
		this.scheduler=scheduler;
	}
	
	public Connector getReadConnector(long key)
	{
		return this.nodes[(int)key%this.nodes.length].getReadConnector();
	}

	public Connector getReadWriteConnector(long key)
	{
		return this.nodes[(int)key%this.nodes.length].getReadWriteConnector();
	}

	public Connector getReadConnector(String key)
	{
		return getReadConnector(Cluster.hash(key));
	}
	public Connector getReadWriteConnector(String key)
	{
		return getReadWriteConnector(Cluster.hash(key));
	}

	public int executeUpdate(Trace parent,String traceCategoryOverride,String sql,Object...parameters) throws Throwable
	{
		Connector connector=null;
		for (Object parameter:parameters)
		{
			if (parameter instanceof LongKey)
			{
				connector=getReadConnector(((LongKey)parameter).key);
			}
			else if (parameter instanceof StringKey)
			{
				connector=getReadConnector(((StringKey)parameter).key);
			}
		}
		if (connector==null)
		{
			throw new Exception("No LongKey or StringKey parameter to use to select partition connector");
		}
		try (Accessor accessor=connector.openAccessor(parent, traceCategoryOverride))
		{
			return accessor.executeUpdate(parent, traceCategoryOverride, sql, parameters);
		}
	}

	public RowSet executeQuery(Trace parent,String traceCategoryOverride,String sql,Object...parameters) throws Throwable
	{
		Connector connector=null;
		for (Object parameter:parameters)
		{
			if (parameter instanceof LongKey)
			{
				connector=getReadConnector(((LongKey)parameter).key);
			}
			else if (parameter instanceof StringKey)
			{
				connector=getReadConnector(((StringKey)parameter).key);
			}
		}
		if (connector==null)
		{
			throw new Exception("No LongKey or StringKey parameter to use to select partition connector");
		}
		try (Accessor accessor=connector.openAccessor(parent, traceCategoryOverride))
		{
			return accessor.executeQuery(parent, traceCategoryOverride, sql, parameters);
		}
	}

	static class FutureQuery implements TraceCallable<RowSet>
	{
		String sql;
		Object[] parameters;
		Connector connector;
		public FutureQuery(Connector connector,String sql,Object[] parameters)
		{
			this.connector=connector;
			this.sql=sql;
			this.parameters=parameters;
		}
		@Override
		public RowSet call(Trace parent) throws Throwable
		{
			try (Accessor accessor=connector.openAccessor(parent, connector.getName()))
			{
				return accessor.executeQuery(parent, null, sql, parameters);
			}
		}
	}

	private Future<RowSet> createQueryFuture(Trace parent,String traceCategoryOverride,String sql,Object[] parameters)
	{
		FutureQuery[] queries=new FutureQuery[this.nodes.length];
		for (int i=0;i<queries.length;i++)
		{
			queries[i]=new FutureQuery(this.nodes[i].getReadConnector(), sql, parameters);
		}
		return this.scheduler.schedule(parent,traceCategoryOverride,queries);
	}
	
	public ClusterRowSet executeClusterQuery(Trace parent,String traceCategoryOverride,String sql,Object...parameters) throws Throwable
	{
		Future<RowSet> future=createQueryFuture(parent, traceCategoryOverride, sql, parameters);
		RowSet[] rowSets=new RowSet[this.nodes.length];
		future.waitAll();
		for (int i=0;i<rowSets.length;i++)
		{
			rowSets[i]=future.getResult(i);
		}
		return new ClusterRowSet(rowSets);
	}

	public ClusterRowSet executeClusterQuery(Trace parent,String traceCategoryOverride,long waitMs,String sql,Object...parameters) throws Throwable
	{
		Future<RowSet> future=createQueryFuture(parent, traceCategoryOverride, sql, parameters);
		RowSet[] rowSets=new RowSet[this.nodes.length];
		future.waitAll(waitMs);
		for (int i=0;i<rowSets.length;i++)
		{
			rowSets[i]=future.getResult(i);
		}
		return new ClusterRowSet(rowSets);
	}

	static class FutureUpdate implements TraceCallable<Integer>
	{
		String sql;
		Object[] parameters;
		Connector connector;
		public FutureUpdate(Connector connector,String sql,Object[] parameters)
		{
			this.connector=connector;
			this.sql=sql;
			this.parameters=parameters;
		}
		@Override
		public Integer call(Trace parent) throws Throwable
		{
			try (Accessor accessor=connector.openAccessor(parent, connector.getName()))
			{
				return accessor.executeUpdate(parent, null, sql, parameters);
			}
		}
	}
	private Future<Integer> createUpdateFuture(Trace parent,String traceCategoryOverride,String sql,Object[] parameters)
	{
		FutureUpdate[] queries=new FutureUpdate[this.nodes.length];
		for (int i=0;i<queries.length;i++)
		{
			queries[i]=new FutureUpdate(this.nodes[i].getReadConnector(), sql, parameters);
		}
		return this.scheduler.schedule(parent,traceCategoryOverride,queries);
	}
	

	public int executeClusterUpdate(Trace parent,String traceCategoryOverride,String sql,Object...parameters) throws Exception
	{
		Future<Integer> future=createUpdateFuture(parent, traceCategoryOverride, sql, parameters);
		future.waitAll();
		int total=0;
		for (int i=0;i<future.size();i++)
		{
			total+=future.getResult(i);
		}
		return total;
	}

}
