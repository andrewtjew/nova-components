package org.nova.parsing.expression;

import org.nova.lexing.Lexeme;

public class IdentifierNode extends ExpressionNode
{
    final private Lexeme lexeme;
    final ArgumentNode argumentNode;
    public IdentifierNode(Lexeme lexeme,ArgumentNode argumentNode)
    {
        this.lexeme=lexeme;
        this.argumentNode=argumentNode;
    }
    public ArgumentNode getArgumentNode()
    {
        return argumentNode;
    }
    public Lexeme getLexeme()
    {
        return this.lexeme;
    }
    public boolean isIdentifier(String identifier)
    {
        return this.lexeme.isIdentifier(identifier);
    }
    public String getIdentifier()
    {
        return (String)this.lexeme.getValue();
    }
}
