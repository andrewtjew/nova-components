package org.nova.parsing.expression;

import org.nova.lexing.Lexeme;

public class KeywordNode extends ExpressionNode
{
    final private Lexeme lexeme;
    public KeywordNode(Lexeme lexeme)
    {
        this.lexeme=lexeme;
    }
    public Lexeme getLexeme()
    {
        return this.lexeme;
    }
    public boolean isKeyword(String keyword)
    {
        return this.lexeme.isIdentifier(keyword);
    }
    public String getKeyword()
    {
        return (String)this.lexeme.getValue();
    }
}
