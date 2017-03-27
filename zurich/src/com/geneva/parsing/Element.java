package com.geneva.parsing;

import java.util.List;

import com.geneva.lexing3.Lexeme;
import com.geneva.lexing3.Token;
import com.geneva.parsing.statement.Statement;

public class Element
{
	final private Lexeme lexeme;
	final private List<Statement> statements;
	public Element(Lexeme lexeme)
	{
		this.lexeme=lexeme;
		this.statements=null;
	}
	Element(List<Statement> statements)
	{
		this.lexeme=null;
		this.statements=statements;
	}
	public Lexeme getLexeme()
	{
		return lexeme;
	}
	public List<Statement> getStatements()
	{
		return statements;
	}
	public boolean isToken(Token token)
	{
		if (lexeme==null)
		{
			return false;
		}
		return lexeme.isToken(token);
	}
}


