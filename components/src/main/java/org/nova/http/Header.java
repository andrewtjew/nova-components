package org.nova.http;

import org.nova.core.KeyValue;

public class Header extends KeyValue<String,String>
{

	public Header(String name, String value) 
	{
		super(name, value);
	}
}
