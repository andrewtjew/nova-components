package com.geneva.parsing;

import com.geneva.lexing3.Lexeme;
import com.geneva.parsing.type.Type;

public class NameType
{
	final private Lexeme name;
	final private Type type;
	public NameType(Lexeme name,Type type)
	{
		this.name=name;
		this.type=type;
	}
	public Lexeme getName()
	{
		return name;
	}
	public Type getType()
	{
		return type;
	}
}
