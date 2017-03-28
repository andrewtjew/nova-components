package com.geneva.parsing.statement;

import java.util.List;

import com.geneva.parsing.Node;


public class ElseIfStatement extends MultiStateme
{
	final private Node expression;

	public ElseIfStatement(Node expression,List<Statement> statements)
    {
	    super(statements);
	    this.expression=expression;
    }

	public Node getExpression()
	{
		return expression;
	}
}
