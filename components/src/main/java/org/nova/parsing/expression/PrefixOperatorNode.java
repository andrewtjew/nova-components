package org.nova.parsing.expression;

import org.nova.lexing.Lexeme;

public class PrefixOperatorNode extends ExpressionNode
{
    final private ExpressionNode operand;
    final private Lexeme lexeme;
    public PrefixOperatorNode(Lexeme lexeme,ExpressionNode operand)
    {
        this.lexeme=lexeme;
        this.operand=operand;
    }
    public ExpressionNode getOperand()
    {
        return operand;
    }
    public Lexeme getLexeme()
    {
        return this.lexeme;
    }
    public boolean isOperator(String operator)
    {
        return this.lexeme.isOperator(operator);
    }
}
