package com.geneva.parsing.statement;

import java.util.List;


public class MultiStateme extends Statement
{
	final private List<Statement> statements;
	public MultiStateme(List<Statement> statements)
	{
		this.statements=statements;
	}
	public List<Statement> getStatements()
	{
		return statements;
	}
	
	
}
