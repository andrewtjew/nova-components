/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
