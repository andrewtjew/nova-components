package org.nova.parsing.expression;

import org.nova.lexing.Lexeme;

public class ConstantNode extends ExpressionNode
{
    final private Lexeme lexeme;
    public ConstantNode(Lexeme lexeme)
    {
        this.lexeme=lexeme;
    }
    public Lexeme getLexeme()
    {
        return this.lexeme;
    }
}
