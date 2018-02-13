package org.nova.parsing.expression;

public class ExpressionNodeError
{
    private static final long serialVersionUID = -4212808083282400952L;

    final private ExpressionNode expressionNode;
    final private String message;
    public ExpressionNodeError(String message,ExpressionNode expressionNode)
    {
        this.message=message;
        this.expressionNode=expressionNode;
    }
    public ExpressionNode getExpressionNode()
    {
        return expressionNode;
    }
    public String getMessage()
    {
        return message;
    }
    
}
