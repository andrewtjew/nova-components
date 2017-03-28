package com.geneva.parsing;

public class NameTypeExpression
{
	final private NameType nameType;
	final private Node expression;
	public NameTypeExpression(NameType nameType,Node expression)
	{
		this.nameType=nameType;
		this.expression=expression;
	}
	public Node getExpression()
	{
		return expression;
	}
	public NameType getNameType()
	{
		return nameType;
	}
	
}
