package com.geneva.parsing;

public class MutableNameTypeExpression
{
	final private MutableNameType mutableNameType;
	final private Node expression;
	public MutableNameTypeExpression(MutableNameType mutableNameType,Node expression)
	{
		this.mutableNameType=mutableNameType;
		this.expression=expression;
	}
	public Node getExpression()
	{
		return expression;
	}
	public MutableNameType getMutableNameType()
	{
		return mutableNameType;
	}
	
}
