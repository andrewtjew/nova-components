package com.geneva.parsing.statement;

import com.geneva.parsing.Node;

public class ReturnStatement extends Statement
{
	final private Node expression;

	public ReturnStatement(Node expression)
    {
	    this.expression=expression;
    }

	public Node getExpression()
	{
		return expression;
	}
}
