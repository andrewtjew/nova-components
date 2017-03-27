package com.geneva.parsing.type;

import java.util.List;

import com.geneva.lexing3.Lexeme;
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
