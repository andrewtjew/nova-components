package org.nova.lexing;

public class CStyleLexer extends Lexer
{
    public CStyleLexer(Source source, String[] operators, String[] seperators)
    {
        super(source
                , new String[]{"=","+","-","*","/","%","++","--","!","==","!=",">",">=","<","<=","&&","||","?",":","~","<<",">>","&","^"}
                , new char[]{';',',','{','}','(',')'}
                , new String[]{"null"}
                , true
                );
    }

    @Override
    public boolean match(char c)
    {
        if (matchComment(c))
        {
            return true;
        }
        if (matchNumber(c))
        {
            return true;
        }
        if (matchCharacter(c))
        {
            return true;
        }
        if (matchDoubleQuotedString(c))
        {
            return true;
        }
        if (matchSeperator(c))
        {
            return true;
        }
        if (matchOperator(c))
        {
            return true;
        }
        if (matchIdentifierOrKeyword(c))
        {
            return true;
        }
        return false;
    }

}
