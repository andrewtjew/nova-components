package org.nova.sqldb;

import java.util.Map;

public class CallResult2<ROW_TYPE,RETURN_TYPE>
{
	final private ROW_TYPE[] results;
	final private Map<Integer,Object> outputValues;
	final private RETURN_TYPE returnValue;
	final private int[] updateCounts;
	
	CallResult2(RETURN_TYPE returnValue,Map<Integer,Object> outValues,ROW_TYPE[] results,int[] updateCounts)
	{
		this.outputValues=outValues;
		this.results=results;
		this.returnValue=returnValue;
		this.updateCounts=updateCounts;
	}
	
	
	public int[] getUpdateCounts()
    {
        return updateCounts;
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
