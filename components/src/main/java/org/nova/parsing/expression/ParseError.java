package org.nova.parsing.expression;

import org.nova.lexing.Lexeme;

public class ParseError
{
    final private Lexeme[] lexemes;
    final private String message;
    public ParseError(String message,Lexeme...lexemes)
    {
        this.message=message;
        this.lexemes=lexemes;
    }
    
    public String getMessage()
    {
        return this.message;
    }
    public Lexeme[] getLexemes()
    {
        return this.lexemes;
    }

}
