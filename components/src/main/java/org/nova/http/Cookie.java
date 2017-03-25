package org.nova.http;

import org.nova.core.KeyValue;

public class Cookie extends KeyValue<String,String>
{

	public Cookie(String name, String value) 
	{
		super(name, value);
	}
}
