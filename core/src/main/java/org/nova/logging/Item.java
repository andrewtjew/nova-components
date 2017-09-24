package org.nova.logging;

public class Item
{
	final private String name;
	final private String value;
	public Item(String name,Object value)
	{
		this.name=name;
		this.value=value!=null?value.toString():null;
	}
	public String getName()
	{
		return name;
	}
	public String getValue()
	{
		return value;
	}

}
