package com.geneva.generation;

import com.geneva.parsing.type.Type;

public class TypeSpecifierBuilder
{
	final private Type type;
	private boolean generic;
	private String fullName;
	
	public TypeSpecifierBuilder(Type type)
	{
		this.type=type;
		this.generic=false;
	}
	public Type getType()
	{
		return type;
	}
	public boolean isGeneric()
	{
		return generic;
	}
	public void setGenericToTrue()
	{
		this.generic = true;
	}
	public String getFullName()
	{
		return fullName;
	}
	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}
	
	
}
