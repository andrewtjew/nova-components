package org.nova.logging;

public class Item
{
	final private String name;
	final private Object value;
	public Item(String name,Object value)
	{
		this.name=name;
		this.value=value;
	}
	public String getName()
	{
		return name;
	}
	public Object getValue()
	{
		return value;
	}

}
