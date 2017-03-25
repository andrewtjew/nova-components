package org.nova.html.pages;

public class Attribute
{
	private final String name;
	private final Object value;
	public Attribute(String name,Object value)
	{
		this.name=name;
		this.value=value;
	}
	public Attribute(String name)
	{
		this.name=name;
		this.value=null;
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
