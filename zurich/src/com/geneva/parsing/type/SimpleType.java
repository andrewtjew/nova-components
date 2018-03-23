package com.geneva.parsing.type;

import com.geneva.parsing.Namespace;

public class SimpleType extends Type
{
	final private Namespace namespace;
	public SimpleType(Namespace namespace)
	{
		this.namespace=namespace;
	}
	public Namespace getNamespace()
	{
		return namespace;
	}
	
}
