package com.geneva.parsing.statement;

import com.geneva.lexing3.Lexeme;

public class LabelStatement extends Statement
{
	final private Lexeme label;
	public LabelStatement(Lexeme label)
	{
		this.label=label;
	}
	public Lexeme getLabel()
	{
		return label;
	}
}
