package org.nova.sqldb;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.nova.tracing.Trace;

public class DataReader<TYPE> implements AutoCloseable
{
	private Accessor accessor;
	final Trace trace;
	final ResultSet resultSet;
	private final Class<TYPE> type;
	
	DataReader(Accessor accessor,Trace trace,ResultSet resultSet,Class<TYPE> type)
	{
		this.type=type;
		this.trace=trace;
		this.resultSet=resultSet;
		this.accessor=accessor;
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
		this.trace.endWait();
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
			this.trace.beginWait();
		}
	}
	
	@Override
	public void close() throws Exception
	{
		synchronized(this)
		{
			if (this.accessor!=null)
			{
				this.resultSet.close();
				this.resultSet.getStatement().close();
				this.trace.close();
				this.accessor=null;
			}
		}
	}


}
