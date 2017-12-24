package org.nova.lexing;

public class Lexeme
{
    private final String literal;
    private final String value;
    private final Token token;
    private final String modifier;
    private final int position;
    private final Source source;

    public Lexeme(Source source,String literal,Token token,String value,int position)
    {
        this(source,literal,token,value,null,position);
    }
    public Lexeme(Source source,String literal,Token token,String value,String modifier,int position)
    {
        this.source=source;
        this.token=token;
        this.literal=literal;
        this.value=value;
        this.modifier=modifier;
        this.position=position;
    } 
    public String getLiteral()
    {
        return this.literal;
    }
    public String getValue()
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
    public int getPosition()
    {
        return position;
    }
    public Source getSource()
    {
        return source;
    }
}
