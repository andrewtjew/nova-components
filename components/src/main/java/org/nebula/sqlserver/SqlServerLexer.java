package org.nebula.sqlserver;

import org.nova.lexing.Lexeme;
import org.nova.lexing.Scanner;
import org.nova.lexing.LexerException;
import org.nova.lexing.Snippet;
import org.nova.lexing.Source;
import org.nova.lexing.Token;

public class SqlServerLexer extends Scanner
{

    public SqlServerLexer(Source source)
    {
        super(source);
        // TODO Auto-generated constructor stub
    }

    public Lexeme expectWordWithBracketsRemoved() throws Throwable
    {
        char c=this.skipWhiteSpaceAndBegin();
        if (c=='[')
        {
            this.begin();
            Lexeme lexeme=produceJavaIdentifier();
            c=this.skipWhiteSpaceAndRead();
            if (c!=']')
            {
                Snippet snippet=this.source.endAndGetSnippet(0);
                return new Lexeme(Token.ERROR, snippet.getTarget(),snippet);
            }
            return lexeme;
        }
        return produceJavaIdentifier();
    }

    public Lexeme expectTableName() throws Throwable
    {
        Lexeme lexeme=expectWordWithBracketsRemoved();
        if (lexeme.isError())
        {
            return lexeme;
        }
        char c=this.skipWhiteSpaceAndBegin();
        if (c=='.')
        {
            lexeme=expectWordWithBracketsRemoved();
            if (lexeme.isError())
            {
                return lexeme;
            }
        }
        return lexeme;
    }
    
    public void skipComments() throws Throwable
    {
        for (;;)
        {
            char c=skipWhiteSpaceAndBegin();
            if ((c!='-')&&(c!='/'))
            {
                this.end(1);
                return;
            }
            if (c=='/')
            {
                this.produceNestableSlashStarComment();
            }
            else 
            {
                c=this.read();
                if (c=='-')
                {
                    this.produceDelimitedText('\n', false);
                }
                else
                {
                    throw new LexerException("Invalid comment", new Lexeme(Token.ERROR,null,this.end(0)));
                }
            }
        }        
    }
}
