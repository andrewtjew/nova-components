package org.nova.sqldb;

import java.util.Map;

public class CallRowSetResult<RETURN_TYPE>
{
	final private RowSet rowSet;
	final private Map<Integer,Object> outputValues;
	final private RETURN_TYPE returnValue;
	final private int updateCount;
	
    CallRowSetResult(RETURN_TYPE returnValue,Map<Integer,Object> outputValues,RowSet rowSet,int updateCount)
	{
		this.returnValue=returnValue;
		this.outputValues=outputValues;
		this.rowSet=rowSet;
		this.updateCount=updateCount;
	}
	
    public int getUpdateCount()
    {
        return updateCount;
    }

	public RowSet getRowSet()
	{
		return rowSet;
	}

	public RETURN_TYPE getReturnValue()
	{
		return this.returnValue;
	}
	@SuppressWarnings("unchecked")
	public <TYPE> TYPE getOutValue(int index)
	{
		return (TYPE)this.outputValues.get(index);
	}
}
