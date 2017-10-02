package org.nova.collections;

import java.util.ArrayList;
import java.util.List;

public class RingBuffer<ITEM>
{
	protected int readIndex;
	protected int writeIndex;
	protected int size;
	
	final protected ITEM[] array;
	final protected  int length;
	
	public RingBuffer(ITEM[] array)
	{
		this.array=array;
		this.length=array.length;
	}
	public void add(ITEM item)
	{
	    if (this.length==0)
	    {
	        return;
	    }
		this.array[this.writeIndex]=item;
		this.writeIndex=(this.writeIndex+1)%this.length;
		if (this.size<this.length)
		{
		    this.size++;
		}
		else
		{
            this.readIndex=(this.readIndex+1)%this.length;
		}
	}
	public int size()
	{
		return this.size;
	}
	public ITEM remove()
	{
		if (size()==0)
		{
			return null;
		}
		try
		{
			return this.array[this.readIndex];
		}
		finally
		{
			this.readIndex=(this.readIndex+1)%this.length;
		}
	}
	public int remove(ITEM[] array)
	{
		int count=array.length>this.size?this.size:array.length;
		for (int i=0;i<count;i++)
		{
			array[i]=this.array[(this.readIndex+i)%this.length];
		}
		this.readIndex=(this.readIndex+count)%this.length;
		this.size-=count;
		return count;
	}
	public int getCapacity()
	{
		return this.array.length;
	}
	public List<ITEM> getSnapshot()
	{
	    int size=size();
	    ArrayList<ITEM> list=new ArrayList<>(size);
		for (int i=0;i<size;i++)
		{
			list.add(this.array[(this.readIndex+i)%this.length]);
		}
		return list;
	}
    public ITEM getFromStart(int index) throws Exception
    {
        if (index>=this.size)
        {
            throw new Exception("Out of range. index="+index+", size="+size);
        }
        return this.array[(this.readIndex+index)%this.length];
    }
    public ITEM getFromEnd(int index) throws Exception
    {
        if (index>=this.size)
        {
            throw new Exception("Out of range. index="+index+", size="+size);
        }
        return this.array[(this.writeIndex+this.length-index)%this.length];
    }
	public void clear()
	{
	    this.readIndex=this.writeIndex=this.size=0;
	}
}
