package org.nebula.sqlserver;

import org.nova.scan.Lexeme;
import org.nova.scan.LexerException;
import org.nova.scan.Scanner;
import org.nova.scan.Snippet;
import org.nova.scan.Source;
import org.nova.scan.Token;

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
            for (c=this.source.next();c!=']';c=this.source.next())
            {
                if (c==0)
                {
                    Snippet snippet=this.source.endAndGetSnippet(0);
                    throw new LexerException("Invalid name", new Lexeme(Token.ERROR, snippet.getTarget(),snippet));
                }
            }
            Snippet snippet=this.source.endAndGetSnippet(1);
            this.source.next();
            return new Lexeme(Token.TEXT, snippet.getTarget(),snippet);
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
