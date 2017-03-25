package org.nova.sqldb;

public class ClusterRowSet
{
	final private RowSet[] rowSets;
	
	public ClusterRowSet(RowSet[] rowSets)
	{
		this.rowSets=rowSets;
	}
	public RowSet getRowSet(int index)
	{
		return this.rowSets[index];
	}
	public RowSet flatten()
	{
		int total=0;
		String[] columnNames=null;
		for (RowSet rowSet:rowSets)
		{
			if (rowSet!=null)
			{
				total+=rowSet.size();
				columnNames=rowSet.columnNames;
			}
		}
		Row[] rows=new Row[total];
		if (columnNames!=null)
		{
			int index=0;
			for (RowSet rowSet:rowSets)
			{
				if (rowSet!=null)
				{
					for (int i=0;i<rowSet.size();i++)
					{
						rows[index++]=rowSet.getRow(i);
					}
				}
			}
		}
		return new RowSet(columnNames,rows); 
	}
}
