package org.nova.sqldb;

import java.sql.Connection;
import java.sql.SQLException;

import org.nova.core.MultiException;
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
	
	private MultiException closeConnection(Throwable t)
	{
        MultiException ex=new MultiException(t);
        try
        {
            Connection connection=this.accessor.connection;
            this.accessor.connection=null;
            connection.close();
        }
        catch (Throwable tt)
        {
            ex=new MultiException(tt,t);
        }
        this.accessor.connector.logger.log(this.trace,this.accessor.connector.getName());
        this.trace.close(ex);
        return ex;
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
                catch (Throwable t)
                {
                    this.accessor.connector.commitFailures.increment();
                    throw closeConnection(t);
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
				catch (Throwable t)
				{
                    this.accessor.connector.rollbackFailures.increment();
                    throw closeConnection(t);
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
