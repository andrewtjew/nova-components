package org.nova.scan;

public class Lexeme
{
    private final String value;
    private final Token token;
    private final Snippet snippet;
    private final String modifier;

    public Lexeme(Token token,String value,Snippet snippet)
    {
        this(token,value,null,snippet);
    }
    public Lexeme(Token token,String value,String modifier,Snippet snippet)
    {
        this.token=token;
        this.value=value;
        this.modifier=modifier;
        this.snippet=snippet;
    }
    public String getValue()
    {
        return value;
    }
    public Token getToken()
    {
        return token;
    }
    public Snippet getSnippet()
    {
        return this.snippet;
    }
    public boolean isOperator(String text)
    {
        if (this.token!=Token.OPERATOR)
        {
            return false;
        }
        return this.value.equals(text);
    }
    public boolean isPunctuator(String text)
    {
        if (this.token!=Token.PUNCTUATOR)
        {
            return false;
        }
        return this.value.equals(text);
    }
    public boolean isString(String text)
    {
        if (this.token!=Token.STRING)
        {
            return false;
        }
        return this.value.equals(text);
    }
    public boolean isCaseInsenstiveWord(String text)
    {
        if (this.token!=Token.TEXT)
        {
            return false;
        }
        return this.value.equalsIgnoreCase(text);
    }
    public boolean isText(String text)
    {
        if (this.token!=Token.TEXT)
        {
            return false;
        }
        return this.value.equals(text);
    }
    public boolean isError()
    {
        return this.token==Token.ERROR;
    }
    public boolean isToken(Token token)
    {
        return (this.token==token);
    }
    public String getModifier()
    {
        return this.modifier;
    }
}
