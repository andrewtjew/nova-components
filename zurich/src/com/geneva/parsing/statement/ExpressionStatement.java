package com.geneva.parsing.statement;

import com.geneva.parsing.MutableNameTypeExpression;

public class ExpressionStatement extends Statement
{
	final private MutableNameTypeExpression mutableNameTypeExpression;
	public ExpressionStatement(MutableNameTypeExpression mutableNameTypeExpression)
	{
		this.mutableNameTypeExpression=mutableNameTypeExpression;
	}
	public MutableNameTypeExpression getMutableNameTypeExpression()
	{
		return mutableNameTypeExpression;
	}


}
