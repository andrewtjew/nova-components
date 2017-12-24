package org.nova.scan;

public class LexerException extends Exception
{
	final private Lexeme lexeme;
	public LexerException(String message,Lexeme lexeme)
	{
		super(message);
		this.lexeme=lexeme;
	}
	public Lexeme getLexeme()
	{
		return lexeme;
	}
	
	
}
