package org.nova.parsing.expression;

import org.nova.lexing.Lexeme;

public class BinaryOperatorNode extends ExpressionNode
{
    final private ExpressionNode leftOperand;
    final private ExpressionNode rightOperand;
    final private Lexeme lexeme;
    public BinaryOperatorNode(Lexeme lexeme,ExpressionNode leftOperand,ExpressionNode rightOperand)
    {
        this.lexeme=lexeme;
        this.leftOperand=leftOperand;
        this.rightOperand=rightOperand;
    }
    public ExpressionNode getLeftOperand()
    {
        return leftOperand;
    }
    public ExpressionNode getRightOperand()
    {
        return rightOperand;
    }
    public Lexeme getLexeme()
    {
        return this.lexeme;
    }
    
    public boolean isOperator(String operator)
    {
        return this.lexeme.isOperator(operator);
    }
    public String getOperator()
    {
        return (String)this.lexeme.getValue();
    }
}
