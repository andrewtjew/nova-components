package org.nova.lexing;

public class Lexeme
{
    private final Object value;
    private final Token token;
    private final String modifier;
    private final int textStart;
    private final int textEnd;
    private final Source source;
    private String literal;

    public Lexeme(Source source,Token token,Object value,int textStart,int textEnd)
    {
        this(source,token,value,null,textStart,textEnd);
    }
    public Lexeme(Source source,Token token,Object value,String modifier,int textStart,int textEnd)
    {
        this.source=source;
        this.token=token;
        this.value=value;
        this.modifier=modifier;
        this.textStart=textStart;
        this.textEnd=textEnd;
    } 
    public String getLiteral()
    {
        if (this.literal==null)
        {
            this.literal=this.source.getText().substring(this.textStart, this.textEnd);
        }
        return this.literal;
    }
    public Object getValue()
    {
        return value;
    }
    public Token getToken()
    {
        return token;
    }
    public String getModifier()
    {
        return this.modifier;
    }
    public int getTextStart()
    {
        return textStart;
    }
    public int getTextEnd()
    {
        return textEnd;
    }
    public Source getSource()
    {
        return source;
    }
    
    public boolean isKeyword(String keyword)
    {
        if (this.token!=Token.KEYWORD)
        {
            return false;
        }
        return keyword.equals((String)this.value);
    }

    public boolean isIdentifier(String identifier)
    {
        if (this.token!=Token.IDENTIFIER)
        {
            return false;
        }
        return identifier.equals((String)this.value);
    }
    
    public boolean isPunctuator(char punctuator)
    {
        if (this.token!=Token.PUNCTUATOR)
        {
            return false;
        }
        return (char)this.value==punctuator;
    }

    public boolean isOperator(String operator)
    {
        if (this.token!=Token.OPERATOR)
        {
            return false;
        }
        return operator.equals((String)this.value);
    }
}
