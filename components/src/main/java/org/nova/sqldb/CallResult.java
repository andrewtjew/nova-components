package org.nova.sqldb;

import java.util.Map;

public class CallResult<RETURN_TYPE>
{
	final private RowSet[] rowSets;
	final private Map<Integer,Object> outputValues;
	final private RETURN_TYPE returnValue;
	final private int[] updateCounts;
	
    CallResult(RETURN_TYPE returnValue,Map<Integer,Object> outputValues,RowSet[] rowSets,int[] updateCounts)
	{
		this.returnValue=returnValue;
		this.outputValues=outputValues;
		this.rowSets=rowSets;
		this.updateCounts=updateCounts;
	}
	
    public int[] getUpdateCounts()
    {
        return updateCounts;
    }

    public RowSet[] getRowSets()
    {
        return rowSets;
    }

    public RowSet getRowSet(int index)
    {
        return this.rowSets[index];
    }

	public <TYPE> TYPE[] getArray(int index,Class<TYPE> type) throws Throwable
	{
	    return Accessor.map(this.rowSets[index], type);
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
