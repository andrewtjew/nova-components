package org.nova.collections;

import java.util.ArrayList;
import java.util.List;

public class RingBuffer<ITEM>
{
	private int readIndex;
	private int writeIndex;
	final private ITEM[] array;
	final private int length;
	public RingBuffer(ITEM[] array)
	{
		this.array=array;
		this.length=array.length;
	}
	public void add(ITEM item)
	{
		this.array[(this.writeIndex+this.length)%this.length]=item;
		this.writeIndex=(this.writeIndex+this.length+1)%this.length;
	}
	public int size()
	{
		return (this.writeIndex+this.length-this.readIndex)%length;
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
			this.readIndex=(this.readIndex+this.length+1)%this.length;
		}
	}
	public int remove(ITEM[] array)
	{
		int size=size();
		int count=array.length>size?size:array.length;
		for (int i=0;i<count;i++)
		{
			array[i]=this.array[(this.readIndex+i)%this.length];
		}
		this.readIndex=(this.readIndex+this.length+count)%this.length;
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
		int count=array.length>size?size:array.length;
		for (int i=0;i<count;i++)
		{
			list.add(this.array[(this.readIndex+i)%this.length]);
		}
		return list;
	}
}
