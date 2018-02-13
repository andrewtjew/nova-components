package org.nova.parsing.expression;

import org.nova.lexing.Lexeme;

public class ArgumentNode extends ExpressionNode
{
    final private Lexeme openLexeme;
    final private Lexeme closeLexeme;
    final private ExpressionNode expressionNode;
    public ArgumentNode(Lexeme openLexeme,Lexeme closeLexeme,ExpressionNode expressionNode)
    {
        this.openLexeme=openLexeme;
        this.closeLexeme=closeLexeme;
        this.expressionNode=expressionNode;
    }
    public Lexeme getOpenLexeme()
    {
        return openLexeme;
    }
    public Lexeme getCloseLexeme()
    {
        return closeLexeme;
    }
    public ExpressionNode getExpressionNode()
    {
        return expressionNode;
    }
    

}
