package com.geneva.parsing;

import com.geneva.lexing3.Lexeme;
import com.geneva.parsing.type.Type;

public class MutableNameType
{
	final private boolean mutable;
	final private Lexeme name;
	final private Type type;
	public MutableNameType(boolean mutable,Lexeme name,Type type)
	{
		this.mutable=mutable;
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
	public boolean isMutable()
	{
		return mutable;
	}
	
}
