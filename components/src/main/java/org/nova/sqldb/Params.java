package org.nova.sqldb;

public class Params
{
	enum Type
	{
		IN,
		OUT,
		IN_OUT,
	}
	
	final Object value;
	final Type type;
	Params next;
	
	Params(Type type,Object value)
	{
		this.value=value;
		this.type=type;
	}
}
