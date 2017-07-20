package org.nova.collections;

import java.util.ArrayList;
import java.util.List;

public class RingBuffer<ITEM>
{
	private int readIndex;
	private int writeIndex;
	private int size;
	
	final private ITEM[] array;
	final private int length;
	
	public RingBuffer(ITEM[] array)
	{
		this.array=array;
		this.length=array.length;
	}
	public void add(ITEM item)
	{
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
}
