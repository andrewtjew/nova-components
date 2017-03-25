package org.nova.sqldb;

import java.util.HashMap;
import java.util.List;

public class RowSet
{
	final String[] columnNames;
	final private Row[] rows;
	RowSet(String[] columnNames,List<Object[]> list)
	{
		this.columnNames=columnNames;
		HashMap<String,Integer> mappings=new HashMap<>();
		for (int i=0;i<columnNames.length;i++)
		{
			mappings.put(columnNames[i], i);
		}
		this.rows=new Row[list.size()];
		for (int i=0;i<this.rows.length;i++)
		{
			this.rows[i]=new Row(mappings,list.get(i));
		}
	}
	RowSet(String[] columnNames,Row[] rows)
	{
		this.columnNames=columnNames;
		this.rows=rows;
	}
	public int getColumns()
	{
		return this.columnNames.length;
	}
	public Row getRow(int index)
	{
		return this.rows[index];
	}
	public String getColumnName(int index)
	{
		return this.columnNames[index];
	}
	public int size()
	{
		return this.rows.length;
	}
}
