package com.geneva.parsing.statement;

import com.geneva.lexing3.Lexeme;

public class GotoStatement extends Statement
{
	final private Lexeme label;
	public GotoStatement(Lexeme label)
	{
		this.label=label;
	}
	public Lexeme getLabel()
	{
		return label;
	}
}
