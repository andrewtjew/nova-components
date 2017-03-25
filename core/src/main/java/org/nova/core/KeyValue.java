package org.nova.core;

public class KeyValue<KEY,VALUE>
{
	final private KEY key;
	final private VALUE value;
	public KeyValue(KEY key,VALUE value)
	{
		this.key=key;
		this.value=value;
	}
	public KEY getName()
	{
		return key;
	}
	public VALUE getValue()
	{
		return value;
	}
		
}
