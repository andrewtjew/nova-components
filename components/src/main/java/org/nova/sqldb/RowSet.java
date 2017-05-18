package org.nova.sqldb;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Row[] getRows()
	{
	    return this.rows;
	}
	public String getColumnName(int index)
	{
		return this.columnNames[index];
	}
	public int size()
	{
		return this.rows.length;
	}
    public <TYPE> TYPE mapSingle(Class<TYPE> type) throws Throwable
    {
        int columns = getColumns();
        
        Map<String,Field> map=FieldMaps.get(type);
        Field[] fields=new Field[columns];
        for (int columnIndex = 0; columnIndex < columns; columnIndex++)
        {
            String name = getColumnName(columnIndex);
            Field field=map.get(name);
            fields[columnIndex]=field;
        }       

        if (size()==0)
        {
            return null;
        }
        Row row=getRows()[0];
        TYPE item = type.newInstance();
        for (int columnIndex = 0; columnIndex < columns; columnIndex++)
        {
            Field field = fields[columnIndex];
            if (field != null)
            {
                field.set(item, row.get(columnIndex));
            }
        }
        return item;
    }
    public <TYPE> TYPE[] mapHack(Class<TYPE> type) throws Throwable
    {
        ArrayList<TYPE> list = new ArrayList<>();
        int columns = getColumns();
        
        Map<String,Field> map=FieldMaps.get(type);
        Field[] fields=new Field[columns];
        for (int columnIndex = 0; columnIndex < columns; columnIndex++)
        {
            String name = getColumnName(columnIndex);
            Field field=map.get(name);
            fields[columnIndex]=field;
        }       

        for (Row row:getRows())
        {
            TYPE item = type.newInstance();
            list.add(item);
            for (int columnIndex = 0; columnIndex < columns; columnIndex++)
            {
                Field field = fields[columnIndex];
                if (field != null)
                {
                    try
                    {
                        field.set(item, row.get(columnIndex));
                    }
                    catch (Throwable t)
                    {
                        if (field.getType()==String.class)
                        {
                            field.set(item, row.get(columnIndex).toString());
                        }
                    }
                }
            }
        }
        return list.toArray((TYPE[]) Array.newInstance(type, list.size()));
    }
    public <TYPE> TYPE[] map(Class<TYPE> type) throws Throwable
    {
        ArrayList<TYPE> list = new ArrayList<>();
        int columns = getColumns();
        
        Map<String,Field> map=FieldMaps.get(type);
        Field[] fields=new Field[columns];
        for (int columnIndex = 0; columnIndex < columns; columnIndex++)
        {
            String name = getColumnName(columnIndex);
            Field field=map.get(name);
            fields[columnIndex]=field;
        }       

        for (Row row:getRows())
        {
            TYPE item = type.newInstance();
            list.add(item);
            for (int columnIndex = 0; columnIndex < columns; columnIndex++)
            {
                Field field = fields[columnIndex];
                if (field != null)
                {
                    try
                    {
                        field.set(item, row.get(columnIndex));
                    }
                    catch (Throwable t)
                    {
                        if (field.getType()==String.class)
                        {
                            field.set(item, row.get(columnIndex).toString());
                        }
                    }
                }
            }
        }
        return list.toArray((TYPE[]) Array.newInstance(type, list.size()));
    }
}
