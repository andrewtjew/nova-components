package org.nova.html.pages;

import com.geneva.util.Source;

public class Lexeme
{
    private final String value;
    private final Token token;
    private final Source source;
    private final int start;
    private final int end;
    private final String message;

    
    public static Lexeme newErrorToken(int start,int end,Source source,String message)
    {
    	return new Lexeme(Token.ERROR,null,start,end,source,message);
    }
    
    public Lexeme(Token token,String value,int start,int end,Source source)
    {
    	this(token,value,start,end,source,null);
    }
    public Lexeme(Token token,String value,int start,int end,Source source,String message)
    {
    	this.token=token;
    	this.value=value;
    	this.start=start;
    	this.end=end;
    	this.source=source;
    	this.message=message;
    }
	public String getValue()
	{
		return value;
	}
	public Token getToken()
	{
		return token;
	}
	public Source getSource()
	{
		return source;
	}
	public int getStart()
	{
		return start;
	}
	public int getEnd()
	{
		return end;
	}
	public String getMessage()
	{
		return message;
	}
    public String getLiteral()
    {
    	return this.source.getText().substring(this.start,this.end);
    }
    public boolean isValue(String text)
    {
    	return text.equals(getValue());
    }
    public boolean isOperator(String text)
    {
    	if (this.token!=Token.OPERATOR)
    	{
    		return false;
    	}
    	return isValue(text);
    }
    public boolean isToken(Token token)
    {
    	return (this.token==token);
    }
}
