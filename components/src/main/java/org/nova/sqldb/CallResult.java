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
