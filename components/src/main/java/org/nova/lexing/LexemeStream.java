package org.nova.lexing;

import java.util.List;

public class LexemeStream
{
    final List<Lexeme> lexemes;
    int index;

    public LexemeStream(List<Lexeme> lexemes)
    {
        this.lexemes=lexemes;
        this.index=0;
    }
    
    public void reset()
    {
        this.index=0;
    }

    public void back(int amount) throws Exception 
    {
        if (this.index<amount)
        {
            throw new Exception();
        }
        this.index-=amount;
    }
    public void forward(int amount) throws Exception 
    {
        if (this.index+amount>=this.lexemes.size())
        {
            throw new Exception();
        }
        this.index+=amount;
    }
    public Lexeme first() throws Exception
    {
        reset();
        return next();
    }
    public int index()
    {
        return this.index;
    }
    public void set(int index)
    {
        this.index=index;
    }
    
    public boolean isEnd()
    {
        return this.index>=this.lexemes.size();
    }
    public Lexeme next() throws Exception
    {
        if (this.index>=this.lexemes.size())
        {
            throw new Exception("End of stream");
        }
        return this.lexemes.get(this.index++);
    }
    public Lexeme current() throws Exception
    {
        if (this.index>=this.lexemes.size())
        {
            throw new Exception("End of stream");
        }
        return this.lexemes.get(this.index);
    }
    
    public boolean forwardToToken(Token token)
    {
        for (;this.index<this.lexemes.size();this.index++)
        {
            if (this.lexemes.get(this.index).getToken()==token)
            {
                return true;
            }
        }
        return false;
    }
    
}
