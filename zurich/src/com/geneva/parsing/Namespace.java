package com.geneva.parsing;

import java.util.List;

import com.geneva.lexing3.Lexeme;

public class Namespace
{
	final private List<Lexeme> lexemes;
	final private String value;
	public Namespace(String value,List<Lexeme> lexemes)
	{
		this.value=value;
		this.lexemes=lexemes;
	}
	public List<Lexeme> getLexemes()
	{
		return lexemes;
	}
	public String getValue()
	{
		return value;
	}
	
}
