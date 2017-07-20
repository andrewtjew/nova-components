package org.nova.http;

import org.nova.core.Pair;

public class Header extends Pair<String,String>
{

	public Header(String name, String value) 
	{
		super(name, value);
	}
}
