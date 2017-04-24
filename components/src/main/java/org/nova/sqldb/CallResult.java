package org.nova.sqldb;

import java.util.Map;

public class CallResult<ROW_TYPE,RETURN_TYPE>
{
	final private ROW_TYPE[] results;
	final private Map<Integer,Object> outputValues;
	final private RETURN_TYPE returnValue;
	final private int updateCount;
	
	CallResult(RETURN_TYPE returnValue,Map<Integer,Object> outValues,ROW_TYPE[] results,int updateCount)
	{
		this.outputValues=outValues;
		this.results=results;
		this.returnValue=returnValue;
		this.updateCount=updateCount;
	}
	
	
	public int getUpdateCount()
    {
        return updateCount;
    }


    public ROW_TYPE[] get()
	{
		return results;
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
