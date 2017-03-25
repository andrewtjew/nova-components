package org.nova.core;

public class NameValue<VALUE> extends KeyValue<String,VALUE>
{

	public NameValue(String key, VALUE value)
	{
		super(key, value);
	}
}
