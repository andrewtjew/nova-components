package org.nova.sqldb;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class DataReader<TYPE> implements AutoCloseable
{
	private AccessorTraceContext traceContext;
	final ResultSet resultSet;
	private final Class<TYPE> type;
	
	DataReader(AccessorTraceContext traceContext,ResultSet resultSet,Class<TYPE> type)
	{
		this.type=type;
		this.resultSet=resultSet;
		this.traceContext=traceContext;
	}

	public ResultSet getResultSet()
	{
		return resultSet;
	}
	
	public int read(TYPE[] array) throws Exception
	{
		return read(array,0,array.length);
	}

	public int read(TYPE[] array,int offset,int amount) throws Exception
	{
		this.traceContext.endWait();
		try
		{
			ResultSetMetaData metaData=resultSet.getMetaData();
			int columns=metaData.getColumnCount();
			int read=0;
			while ((offset+read<array.length)&&(resultSet.next()))
			{
				TYPE item=this.type.newInstance();
				for (int columnIndex=0;columnIndex<columns;columnIndex++)
				{
					String name=metaData.getColumnName(columnIndex+1);
					
					//@Name annotation is not needed. Just use SELECT key as classFieldName etc
					Field field=type.getDeclaredField(name);
					if (field!=null)
					{
						boolean accessible=field.isAccessible();
						if (accessible==false)
						{
							field.setAccessible(true);
						}
						try
						{
							field.set(item, resultSet.getObject(columnIndex+1));
						}
						finally
						{
							if (accessible==false)
							{
								field.setAccessible(false);
							}
						}
					}
				}
				array[offset+read]=item;
				read++;
			}
			return read;
		}
		finally
		{
			this.traceContext.beginWait();
		}
	}
	
	@Override
	public void close() throws Exception
	{
		synchronized(this)
		{
			if (this.traceContext!=null)
			{
				this.resultSet.close();
				this.resultSet.getStatement().close();
				this.traceContext.close();
				this.traceContext=null;
			}
		}
	}


}
