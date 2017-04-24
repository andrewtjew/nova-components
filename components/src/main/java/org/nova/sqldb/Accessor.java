package org.nova.sqldb;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nova.collections.Pool;
import org.nova.collections.Resource;
import org.nova.core.MultiException;
import org.nova.sqldb.Param.Direction;
import org.nova.tracing.Trace;

public class Accessor extends Resource
{
	final private long connectionIdleTimeout;
	final private Connector connector;
	private Connection connection;
	private long lastExecute;
	private Transaction transaction;

	public Accessor(Pool<Accessor> pool, Connector connector, long connectionIdleTimeout)
	{
		super(pool);
		this.connectionIdleTimeout = connectionIdleTimeout;
		this.connector = connector;
	}

	public Transaction beginTransaction(Trace parent, String traceCategory) throws Exception
	{
		synchronized (this)
		{
			if (this.transaction != null)
			{
				throw new Exception("Cannot begin more than one transaction. Existing transaction category=" + transaction.trace.getCategory());
			}
			this.transaction = new Transaction(this, new Trace(this.connector.traceManager, parent, traceCategory));
			this.connection.setAutoCommit(false);
			return this.transaction;
		}
	}

	private Throwable closeConnection(Throwable cause)
	{
		try
		{
			this.connection.close();
		}
		catch (Throwable t)
		{
			return new MultiException(cause, t);
		}
		finally
		{
			this.connection = null;
		}
		return cause;
	}

	void commit() throws Throwable
	{
		synchronized (this)
		{
			if (transaction != null)
			{
				try
				{
					this.connection.commit();
					this.connection.setAutoCommit(true);
				}
				catch (Throwable t)
				{
					throw closeConnection(t);
				}
				finally
				{
					this.transaction = null;
				}
			}
		}
	}

	void rollback() throws Throwable
	{
		synchronized (this)
		{
			try
			{
				this.connection.rollback();
				this.connection.setAutoCommit(true);
			}
			catch (Throwable t)
			{
				throw closeConnection(t);
			}
			finally
			{
				this.transaction = null;
			}
		}
	}

	@Override
	protected void activate() throws Throwable
	{
		long now = System.currentTimeMillis();
		if (this.connection != null)
		{
			if (now - this.lastExecute < connectionIdleTimeout)
			{
				return;
			}
			try
			{
				this.connection.close();
			}
			catch (SQLException e)
			{
				// log too
				this.connector.closeConnectionExceptions.increment();
			}
			this.connection = null;
		}
		try
		{
			this.connection = this.connector.createConnection();
			this.lastExecute = now;
		}
		catch (Throwable e)
		{
			this.connector.createConnectionExceptions.increment();
			throw e;
		}
	}

	@Override
	protected void park() throws Throwable
	{
	}
	public int executeUpdate(Trace parent, String traceCategoryOverride, String sql, Object... parameters) throws Throwable
	{
		return executeUpdate(parent, traceCategoryOverride, parameters, sql);
	}
    public int executeUpdate(Trace parent, String traceCategoryOverride, List<?> parameters,String sql) throws Throwable
    {
        return executeUpdate(parent, traceCategoryOverride, parameters.toArray(new Object[parameters.size()]), sql);
    }
	public int executeUpdate(Trace parent, String traceCategoryOverride, Object[] parameters,String sql) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = sql;
		}
		try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
		{
			try
			{
				try (PreparedStatement statement = this.connection.prepareStatement(sql))
				{
					for (int i = 0; i < parameters.length; i++)
					{
						statement.setObject(i + 1, parameters[i]);
					}
					try
					{
						return statement.executeUpdate();
					}
					finally
					{
						this.lastExecute = System.currentTimeMillis();
					}
				}
			}
			catch (SQLException ex)
			{
				trace.close(ex);
				throw closeConnection(ex);
			}

		}
	}
    public GeneratedKeys executeUpdateAndReturnGeneratedKeys(Trace parent, String traceCategoryOverride, String sql, Object... parameters) throws Throwable
    {
        return executeUpdateAndReturnGeneratedKeys(parent, traceCategoryOverride,parameters, sql);
    }
    public GeneratedKeys executeUpdateAndReturnGeneratedKeys(Trace parent, String traceCategoryOverride, List<?> parameters,String sql) throws Throwable
    {
        return executeUpdateAndReturnGeneratedKeys(parent, traceCategoryOverride,parameters.toArray(new Object[parameters.size()]), sql);
    }
    public GeneratedKeys executeUpdateAndReturnGeneratedKeys(Trace parent, String traceCategoryOverride, Object[] parameters,String sql) throws Throwable
    {
        if (traceCategoryOverride == null)
        {
            traceCategoryOverride = sql;
        }
        try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
        {
            try
            {
                try (PreparedStatement statement = this.connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS))
                {
                    for (int i = 0; i < parameters.length; i++)
                    {
                        statement.setObject(i + 1, parameters[i]);
                    }
                    try
                    {
                        int rowsAffected=statement.executeUpdate();
                        if (rowsAffected<=0)
                        {
                            return null;
                        }
                        ResultSet resultSet=statement.getGeneratedKeys();
                        ArrayList<BigDecimal> list=new ArrayList<>();
                        while (resultSet.next())
                        {
                            list.add((BigDecimal)resultSet.getObject(1));
                        }
                        return new GeneratedKeys(list);
                    }
                    finally
                    {
                        this.lastExecute = System.currentTimeMillis();
                    }
                }
            }
            catch (SQLException ex)
            {
                trace.close(ex);
                throw closeConnection(ex);
            }

        }
    }
    public int[] executeBatchUpdate(Trace parent, String traceCategoryOverride, String sql,Object[]... batchParameters) throws Throwable
    {
        return executeBatchUpdate(parent, traceCategoryOverride, batchParameters, sql);
    }    
    public int[] executeBatchUpdate(Trace parent, String traceCategoryOverride,List<List<?>> batchParameters,String sql) throws Throwable
    {
        Object[][] arrays=new Object[batchParameters.size()][];
        for (int i=0;i<batchParameters.size();i++)
        {
            List<?> list=batchParameters.get(i);
            arrays[i]=list.toArray(new Object[list.size()]);
        }
        return executeBatchUpdate(parent, traceCategoryOverride, arrays, sql);
    }    
	public int[] executeBatchUpdate(Trace parent, String traceCategoryOverride,Object[][] batchParameters,String sql) throws Throwable
    {
        if (traceCategoryOverride == null)
        {
            traceCategoryOverride = sql;
        }
        try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
        {
            try
            {
                try (PreparedStatement statement = this.connection.prepareStatement(sql))
                {
                    for (int j = 0; j < batchParameters.length; j++)
                    {
                        Object[] parameters=batchParameters[j];
                        for (int i = 0; i < parameters.length; i++)
                        {
                            statement.setObject(i + 1, parameters[i]);
                        }
                        statement.addBatch();
                    }
                    try
                    {
                        return statement.executeBatch();
                    }
                    finally
                    {
                        this.lastExecute = System.currentTimeMillis();
                    }
                }
            }
            catch (SQLException ex)
            {
                trace.close(ex);
                throw closeConnection(ex);
            }

        }
    }

	
	@SuppressWarnings("unchecked")
	private <TYPE> TYPE[] convert(ResultSet resultSet,Class<TYPE> type) throws Throwable
	{
		ArrayList<TYPE> list = new ArrayList<>();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columns = metaData.getColumnCount();
		
		Map<String,Field> map=FieldMaps.get(type);
		Field[] fields=new Field[columns];
		for (int columnIndex = 0; columnIndex < columns; columnIndex++)
		{
			String name = metaData.getColumnName(columnIndex + 1);
			Field field=map.get(name);
			fields[columnIndex]=field;
		}		
		
		while (resultSet.next())
		{
			TYPE item = type.newInstance();
			list.add(item);
			for (int columnIndex = 0; columnIndex < columns; columnIndex++)
			{
				Field field = fields[columnIndex];
				if (field != null)
				{
					field.set(item, resultSet.getObject(columnIndex + 1));
				}
			}
		}
		return list.toArray((TYPE[]) Array.newInstance(type, list.size()));
	}
	private <TYPE> TYPE convertSingle(ResultSet resultSet,Class<TYPE> type) throws Throwable
	{
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columns = metaData.getColumnCount();
		
		Map<String,Field> map=FieldMaps.get(type);
		if (resultSet.next())
		{
			TYPE item = type.newInstance();
			for (int columnIndex = 0; columnIndex < columns; columnIndex++)
			{
				String name = metaData.getColumnName(columnIndex + 1);
				Field field = map.get(name);
				if (field != null)
				{
					field.set(item, resultSet.getObject(columnIndex + 1));
				}
			}
			return item;
		}
		return null;
	}
	private RowSet convert(ResultSet resultSet) throws SQLException
	{
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columns = metaData.getColumnCount();
		String[] columnNames = new String[columns];
		for (int columnIndex = 0; columnIndex < columns; columnIndex++)
		{
			columnNames[columnIndex] = metaData.getColumnName(columnIndex + 1);
		}
		ArrayList<Object[]> list = new ArrayList<>();
		while (resultSet.next())
		{
			Object[] rowItems = new Object[columns];
			for (int columnIndex = 0; columnIndex < columns; columnIndex++)
			{
				rowItems[columnIndex] = resultSet.getObject(columnIndex + 1);
			}
			list.add(rowItems);
		}
		return new RowSet(columnNames, list);
	}

	public RowSet executeQuery(Trace parent, String traceCategoryOverride, String sql, Object... parameters) throws Throwable
	{
		return executeQuery(parent, traceCategoryOverride, parameters, sql);
	}
    public RowSet executeQuery(Trace parent, String traceCategoryOverride, List<?> parameters, String sql) throws Throwable
    {
        return executeQuery(parent, traceCategoryOverride, parameters.toArray(new Object[parameters.size()]), sql);
    }
	public RowSet executeQuery(Trace parent, String traceCategoryOverride, Object[] parameters, String sql) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = sql;
		}
		try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
		{
			try
			{
				try (PreparedStatement statement = this.connection.prepareStatement(sql))
				{
					for (int i = 0; i < parameters.length; i++)
					{
						statement.setObject(i + 1, parameters[i]);
					}
					if (statement.execute() == false)
					{
						return null;
					}
					try (ResultSet resultSet = statement.getResultSet())
					{
						return convert(resultSet);
					}
				}
			}
			catch (SQLException ex)
			{
				trace.close(ex);
				throw closeConnection(ex);
			}

		}
	}

	public <TYPE> TYPE executeQuerySingle(Trace parent, String traceCategoryOverride, Class<TYPE> type, String sql,Object...parameters) throws Throwable
	{
		return executeQuerySingle(parent, traceCategoryOverride, type, parameters,sql);
	}	
    public <TYPE> TYPE executeQuerySingle(Trace parent, String traceCategoryOverride, Class<TYPE> type, List<?> parameters, String sql) throws Throwable
    {
        return executeQuerySingle(parent, traceCategoryOverride, type, parameters.toArray(new Object[parameters.size()]),sql);
    }   
	public <TYPE> TYPE executeQuerySingle(Trace parent, String traceCategoryOverride, Class<TYPE> type, Object[] parameters, String sql) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = sql;
		}
		try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
		{
			try
			{
				try (PreparedStatement statement = this.connection.prepareStatement(sql))
				{
					for (int i = 0; i < parameters.length; i++)
					{
						statement.setObject(i + 1, parameters[i]);
					}
					try
					{
						if (statement.execute() == false)
						{
							return null;
						}
					}
					finally
					{
						this.lastExecute = System.currentTimeMillis();
					}
					try (ResultSet resultSet = statement.getResultSet())
					{
						return convertSingle(resultSet,type);
					}
				}
			}
			catch (SQLException ex)
			{
				trace.close(ex);
				throw closeConnection(ex);
			}
		}
	}
	public <TYPE> TYPE[] executeQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, String sql, Object... parameters) throws Throwable
	{
		return executeQuery(parent, traceCategoryOverride, type, parameters, sql);
	}
    public <TYPE> TYPE[] executeQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, List<?> parameters, String sql) throws Throwable
    {
        return executeQuery(parent, traceCategoryOverride, type, parameters.toArray(new Object[parameters.size()]), sql);
    }
	public <TYPE> TYPE[] executeQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, Object[] parameters, String sql) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = sql;
		}
		try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
		{
			try
			{
				try (PreparedStatement statement = this.connection.prepareStatement(sql))
				{
					for (int i = 0; i < parameters.length; i++)
					{
						statement.setObject(i + 1, parameters[i]);
					}
					try
					{
						if (statement.execute() == false)
						{
							return null;
						}
					}
					finally
					{
						this.lastExecute = System.currentTimeMillis();
					}
					try (ResultSet resultSet = statement.getResultSet())
					{
						return convert(resultSet,type);
					}
				}
			}
			catch (SQLException ex)
			{
				trace.close(ex);
				throw closeConnection(ex);
			}
		}
	}
	public <TYPE> TYPE executeCallSingle(Trace parent, String traceCategoryOverride, Class<TYPE> type, String sql, Object... parameters) throws Throwable
	{
		return executeCallSingle(parent,traceCategoryOverride,type,parameters,sql);
	}
    public <TYPE> TYPE executeCallSingle(Trace parent, String traceCategoryOverride, Class<TYPE> type, List<?> parameters, String sql) throws Throwable
    {
        return executeCallSingle(parent,traceCategoryOverride,type,parameters.toArray(new Object[parameters.size()]),sql);
    }
	public <TYPE> TYPE executeCallSingle(Trace parent, String traceCategoryOverride, Class<TYPE> type, Object[] parameters, String sql) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = sql;
		}
		try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
		{
			try
			{
				try (CallableStatement statement = this.connection.prepareCall(sql))
				{
					for (int i = 0; i < parameters.length; i++)
					{
						statement.setObject(i + 1, parameters[i]);
					}
					try
					{
						if (statement.execute() == false)
						{
							return null;
						}
					}
					finally
					{
						this.lastExecute = System.currentTimeMillis();
					}
					try (ResultSet resultSet = statement.getResultSet())
					{
						return convertSingle(resultSet,type);
					}
				}
			}
			catch (SQLException ex)
			{
				trace.close(ex);
				throw closeConnection(ex);
			}
		}
	}

	public <TYPE> TYPE[] executeArrayQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, String sql, Object... parameters) throws Throwable
	{
		return executeArrayQuery(parent, traceCategoryOverride, type, parameters, sql);
	}
    public <TYPE> TYPE[] executeArrayQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, List<?> parameters, String sql) throws Throwable
    {
        return executeArrayQuery(parent, traceCategoryOverride, type, parameters.toArray(new Object[parameters.size()]), sql);
    }
	public <TYPE> TYPE[] executeArrayQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, Object[] parameters, String sql) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = sql;
		}
		try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
		{
			try
			{
				try (CallableStatement statement = this.connection.prepareCall(sql))
				{
					for (int i = 0; i < parameters.length; i++)
					{
						statement.setObject(i + 1, parameters[i]);
					}
					try
					{
						if (statement.execute() == false)
						{
							return null;
						}
					}
					finally
					{
						this.lastExecute = System.currentTimeMillis();
					}
					try (ResultSet resultSet = statement.getResultSet())
					{
						return convert(resultSet,type);
					}
				}
			}
			catch (SQLException ex)
			{
				trace.close(ex);
				throw closeConnection(ex);
			}
		}
	}

	@SuppressWarnings("unchecked")
    public <RETURN_TYPE> CallRowSetResult<RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<RETURN_TYPE> returnType,String name,Param...parameters) throws Throwable
    {
        return executeCall(parent,traceCategoryOverride,returnType,parameters,name);
    }
    public <RETURN_TYPE> CallRowSetResult<RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<RETURN_TYPE> returnType,List<Param> parameters,String name) throws Throwable
    {
        return executeCall(parent,traceCategoryOverride,returnType,parameters.toArray(new Param[parameters.size()]),name);
    }
    public <RETURN_TYPE> CallRowSetResult<RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<RETURN_TYPE> returnType,Param[] parameters,String name) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = name;
		}
		StringBuilder sb=new StringBuilder();
		sb.append('{');
		int offset=1;
		if ((returnType!=Void.class)&&(returnType!=void.class))
		{
			offset=2;
			sb.append("?=");
		}
		sb.append("call ").append(name);
		if (parameters.length>0)
		{
			sb.append('(');
			for (int i = 0; i < parameters.length; i++)
			{
				if (i>0)
				{
					sb.append(',');
				}
				sb.append('?');
			}
			sb.append(')');
		}
		sb.append('}');
		String sql=sb.toString();
		try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
		{
			try
			{
				try (CallableStatement statement = this.connection.prepareCall(sql))
				{
					if (offset==2)
					{
						int returnSqlType=Param.getSqlType(returnType);
						statement.registerOutParameter(1,returnSqlType);
					}
					for (int i = 0; i < parameters.length; i++)
					{
						Param param=parameters[i];
						if ((param.direction==Direction.IN)||(param.direction==Direction.IN_OUT))
						{
							statement.setObject(i + offset, parameters[i].inValue);
						}
						if ((param.direction==Direction.OUT)||(param.direction==Direction.IN_OUT))
						{
							statement.registerOutParameter(i+offset, param.sqlType);
						}
					}
					boolean executeResult=statement.execute();
					int updateCount=0;
                    RETURN_TYPE returnValue=null;
                    HashMap<Integer,Object> outValues=new HashMap<>();
                    if (offset==2)
                    {
                        returnValue=(RETURN_TYPE)statement.getObject(1);
                    }

                    if (executeResult == false)
					{
					    updateCount=statement.getUpdateCount();
					    if (updateCount==0)
					    {
                            for (int i = 0; i < parameters.length; i++)
                            {
                                Param param=parameters[i];
                                if ((param.direction==Direction.OUT)||(param.direction==Direction.IN_OUT))
                                {
                                    outValues.put(i, statement.getObject(i+offset));
                                }
                            }
                            return new CallRowSetResult<RETURN_TYPE>(returnValue,outValues,null,updateCount);
					    }
                        boolean more=statement.getMoreResults();
                        if (more==false)
                        {
                            return new CallRowSetResult<RETURN_TYPE>(returnValue,outValues,null,updateCount);
                        }
					}
                    
					try (ResultSet resultSet = statement.getResultSet())
					{
					    if (resultSet!=null)
					    {
    						RowSet results=convert(resultSet);
    						for (int i = 0; i < parameters.length; i++)
    						{
    							Param param=parameters[i];
    							if ((param.direction==Direction.OUT)||(param.direction==Direction.IN_OUT))
    							{
    								outValues.put(i, statement.getObject(i+offset));
    							}
    						}
                            return new CallRowSetResult<RETURN_TYPE>(returnValue,outValues,results,updateCount);
					    }
                        return new CallRowSetResult<RETURN_TYPE>(returnValue,outValues,null,updateCount);
					}
					
				}
			}
			catch (SQLException ex)
			{
				trace.close(ex);
				throw closeConnection(ex);
			}
		}
	}
	/*
	public <TYPE> CallResult<TYPE,Void> executeCall(Trace parent, String traceCategoryOverride, Class<TYPE> type,String name,Param...parameters) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = name;
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{call ").append(name);
		if (parameters.length>0)
		{
			sb.append('(');
			for (int i = 0; i < parameters.length; i++)
			{
				if (i>0)
				{
					sb.append(',');
				}
				sb.append('?');
			}
			sb.append(')');
		}
		sb.append('}');
		String sql=sb.toString();
		try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
		{
			try
			{
				try (CallableStatement statement = this.connection.prepareCall(sql))
				{
					for (int i = 0; i < parameters.length; i++)
					{
						Param param=parameters[i];
						if ((param.direction==Direction.IN)||(param.direction==Direction.IN_OUT))
						{
							statement.setObject(i + offset, parameters[i].inValue);
						}
						if ((param.direction==Direction.OUT)||(param.direction==Direction.IN_OUT))
						{
							statement.registerOutParameter(i+offset, param.sqlType);
						}
					}
					if (statement.execute() == false)
					{
						return null;
					}
					try (ResultSet resultSet = statement.getResultSet())
					{
						TYPE[] results=convert(resultSet,type);
						HashMap<Integer,Object> outputMap=new HashMap<>();
						for (int i = 0; i < parameters.length; i++)
						{
							Param param=parameters[i];
							if ((param.direction==Direction.OUT)||(param.direction==Direction.IN_OUT))
							{
								outputMap.put(i, statement.getObject(i+1));
							}
						}
						return new CallResult<TYPE,Void>(outputMap,results,Void.class);
					}
				}
			}
			catch (SQLException ex)
			{
				trace.close(ex);
				throw closeConnection(ex);
			}
		}
	}
	*/
    public <ROW_TYPE,RETURN_TYPE> CallResult<ROW_TYPE,RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<ROW_TYPE> rowType,Class<RETURN_TYPE> returnType,String name,Param...parameters) throws Throwable
    {
        return executeCall(parent, traceCategoryOverride, rowType, returnType, parameters,name);
    }
    public <ROW_TYPE,RETURN_TYPE> CallResult<ROW_TYPE,RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<ROW_TYPE> rowType,Class<RETURN_TYPE> returnType,List<Param> parameters,String name) throws Throwable
    {
        return executeCall(parent, traceCategoryOverride, rowType, returnType, parameters.toArray(new Param[parameters.size()]),name);
    }
	@SuppressWarnings("unchecked")
	public <ROW_TYPE,RETURN_TYPE> CallResult<ROW_TYPE,RETURN_TYPE> executeCall(Trace parent, String traceCategoryOverride, Class<ROW_TYPE> rowType,Class<RETURN_TYPE> returnType,Param[] parameters,String name) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = name;
		}
		StringBuilder sb=new StringBuilder();
		sb.append('{');
		int offset=1;
		if ((returnType!=Void.class)&&(returnType!=void.class))
		{
			offset=2;
			sb.append("?=");
		}
		sb.append("call ").append(name);
		if (parameters.length>0)
		{
			sb.append('(');
			for (int i = 0; i < parameters.length; i++)
			{
				if (i>0)
				{
					sb.append(',');
				}
				sb.append('?');
			}
			sb.append(')');
		}
		sb.append('}');
		String sql=sb.toString();
		try (Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride))
		{
			try
			{
				try (CallableStatement statement = this.connection.prepareCall(sql))
				{
					if (offset==2)
					{
						int returnSqlType=Param.getSqlType(returnType);
						statement.registerOutParameter(1,returnSqlType);
					}
					for (int i = 0; i < parameters.length; i++)
					{
						Param param=parameters[i];
						if ((param.direction==Direction.OUT)||(param.direction==Direction.IN_OUT))
						{
							statement.registerOutParameter(i+offset, param.sqlType);
						}
                        if ((param.direction==Direction.IN)||(param.direction==Direction.IN_OUT))
                        {
                            statement.setObject(i + offset, parameters[i].inValue);
                        }
					}
                    boolean executeResult=statement.execute();
                    int updateCount=0;
                    RETURN_TYPE returnValue=null;
                    HashMap<Integer,Object> outValues=new HashMap<>();
                    if (offset==2)
                    {
                        returnValue=(RETURN_TYPE)statement.getObject(1);
                    }

					if (executeResult == false)
					{
                        updateCount=statement.getUpdateCount();
                        if (updateCount==0)
                        {
                            for (int i = 0; i < parameters.length; i++)
                            {
                                Param param=parameters[i];
                                if ((param.direction==Direction.OUT)||(param.direction==Direction.IN_OUT))
                                {
                                    outValues.put(i, statement.getObject(i+offset));
                                }
                            }
                            return new CallResult<ROW_TYPE,RETURN_TYPE>(returnValue,outValues,null,updateCount);
                        }
                        boolean more=statement.getMoreResults();
                        if (more==false)
                        {
                            return new CallResult<ROW_TYPE,RETURN_TYPE>(returnValue,outValues,null,updateCount);
                        }
					}
					try (ResultSet resultSet = statement.getResultSet())
					{
						ROW_TYPE[] results=convert(resultSet,rowType);
						for (int i = 0; i < parameters.length; i++)
						{
							Param param=parameters[i];
							if ((param.direction==Direction.OUT)||(param.direction==Direction.IN_OUT))
							{
								outValues.put(i, statement.getObject(i+offset));
							}
						}
						return new CallResult<ROW_TYPE,RETURN_TYPE>(returnValue,outValues,results,updateCount);
					}
				}
			}
			catch (SQLException ex)
			{
				trace.close(ex);
				throw closeConnection(ex);
			}
		}
	}
	

	@SuppressWarnings("unchecked")
    public <TYPE> DataReader<TYPE> openQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, String sql, Object... parameters) throws Throwable
    {
	    return openQuery(parent, traceCategoryOverride, type, parameters,sql);
    }
    public <TYPE> DataReader<TYPE> openQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, List<?> parameters, String sql) throws Throwable
    {
        return openQuery(parent, traceCategoryOverride, type, parameters.toArray(new Object[parameters.size()]),sql);
    }
	public <TYPE> DataReader<TYPE> openQuery(Trace parent, String traceCategoryOverride, Class<TYPE> type, Object[] parameters, String sql) throws Throwable
	{
		if (traceCategoryOverride == null)
		{
			traceCategoryOverride = sql;
		}
		Trace trace = new Trace(this.connector.traceManager, parent, traceCategoryOverride, true);
		try
		{
			PreparedStatement statement = this.connection.prepareStatement(sql);
			try
			{
				for (int i = 0; i < parameters.length; i++)
				{
					statement.setObject(i + 1, parameters[i]);
				}
				return new DataReader<TYPE>(this, trace, statement.getResultSet(), type);
			}
			catch (Throwable t)
			{
				try
				{
					statement.close();
				}
				catch (Throwable tt)
				{
					trace.close(tt);
					throw tt;
				}
				trace.close(t);
				throw t;
			}
		}
		catch (Throwable t)
		{
			trace.close(t);
			throw closeConnection(t);
		}
	}
}
