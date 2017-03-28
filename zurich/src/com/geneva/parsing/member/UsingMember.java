package com.geneva.parsing.member;

import com.geneva.lexing3.Lexeme;
import com.geneva.parsing.Namespace;

public class UsingMember extends Member
{
	final private Lexeme alias;
	final private Namespace namespace;
	public UsingMember(Lexeme alias,Namespace namespace)
	{
		this.alias=alias;
		this.namespace=namespace;
	}
	public Lexeme getAlias()
	{
		return alias;
	}
	public Namespace getNamespace()
	{
		return namespace;
	}
	
}
