package org.nova.sqldb;

import java.sql.SQLException;

import org.nova.tracing.Trace;

public class Transaction implements AutoCloseable
{
	private Accessor accessor;
	final Trace trace;
	
	Transaction(Accessor accessor,Trace trace)
	{
		this.trace=trace;
		this.accessor=accessor;
	}
	
	public void commit() throws Throwable
	{
		synchronized(this)
		{
			if (this.accessor!=null)
			{
				try
				{
					this.accessor.commit();
				}
				finally
				{
					this.trace.close();
					this.accessor=null;
				}
			}
		}
	}
	
	public void rollback() throws Throwable
	{
		synchronized(this)
		{
			if (this.accessor!=null)
			{
				try
				{
					this.accessor.rollback();
				}
				finally
				{
					this.trace.close();
					this.accessor=null;
				}
			}
		}
			
	}
	
	@Override
	public void close() throws Exception
	{
		try
		{
			rollback();
		}
		catch (Throwable t)
		{
			throw new Exception(t);
		}
	}

}
