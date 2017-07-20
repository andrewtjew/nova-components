package org.nova.core;

public class Pair<NAME,VALUE>
{
	final private NAME key;
	final private VALUE value;
	public Pair(NAME key,VALUE value)
	{
		this.key=key;
		this.value=value;
	}
	public NAME getName()
	{
		return key;
	}
	public VALUE getValue()
	{
		return value;
	}
		
}
