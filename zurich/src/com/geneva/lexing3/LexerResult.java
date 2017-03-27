package com.geneva.lexing3;

public class LexerResult
{
	private final Lexeme[] lexemes;
	private final int count;
	public LexerResult(Lexeme[] lexemes,int count)
	{
		this.lexemes=lexemes;
		this.count=count;
	}
	public Lexeme[] getLexemes()
	{
		return lexemes;
	}
	public int getCount()
	{
		return count;
	}
	
}
