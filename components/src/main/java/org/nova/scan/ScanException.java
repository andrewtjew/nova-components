package org.nova.scan;

public class ScanException extends Exception
{
	final private Lexeme lexeme;
	public ScanException(String message,Lexeme lexeme)
	{
		super(message);
		this.lexeme=lexeme;
	}
	public Lexeme getLexeme()
	{
		return lexeme;
	}	
}
