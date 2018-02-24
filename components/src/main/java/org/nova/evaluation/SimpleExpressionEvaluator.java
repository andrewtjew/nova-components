package org.nova.evaluation;

import java.util.List;

import org.nova.lexing.Token;
import org.nova.parsing.expression.BinaryOperatorNode;
import org.nova.parsing.expression.ConstantNode;
import org.nova.parsing.expression.ExpressionNode;
import org.nova.parsing.expression.ParseError;
import org.nova.parsing.expression.PrefixOperatorNode;

public class SimpleExpressionEvaluator
{
    public static double evaluateToDouble(ExpressionNode node,List<ParseError> parseErrors)
    {
        if (node instanceof ConstantNode)
        {
            ConstantNode n=(ConstantNode)node;
            if (n.getLexeme().getToken()==Token.INTEGER)
            {
                return (long)n.getLexeme().getValue();
            }
            else if (n.getLexeme().getToken()==Token.FLOAT)
            {
                return (double)n.getLexeme().getValue();
            }
            parseErrors.add(new ParseError("Constant is not an number.",n.getLexeme()));
            return 0.0;
        } 
        else if (node instanceof BinaryOperatorNode)
        {
            BinaryOperatorNode n=(BinaryOperatorNode)node;
            if (n.isOperator("+"))
            {
                double left=evaluateToDouble(n.getLeftOperand(), parseErrors);
                double right=evaluateToDouble(n.getRightOperand(), parseErrors);
                return left+right;
            }
            else if (n.isOperator("-"))
            {
                double left=evaluateToDouble(n.getLeftOperand(), parseErrors);
                double right=evaluateToDouble(n.getRightOperand(), parseErrors);
                return left-right;
            }
            else if (n.isOperator("/"))
            {
                double left=evaluateToDouble(n.getLeftOperand(), parseErrors);
                double right=evaluateToDouble(n.getRightOperand(), parseErrors);
                return left/right;
            }
            /*
             * how does % work on floats?
            else if (n.isOperator("%"))
            {
                double left=evaluateToDouble(n.getLeftOperand(), parseErrors);
                double right=evaluateToDouble(n.getRightOperand(), parseErrors);
                return left%right;
            }
            */
            parseErrors.add(new ParseError("Invalid operator",n.getLexeme()));
            return 0.0;
        }
        else if (node instanceof PrefixOperatorNode)
        {
            PrefixOperatorNode n=(PrefixOperatorNode)node;
            if (n.isOperator("+"))
            {
                return evaluateToDouble(n.getOperand(), parseErrors);
            }
            else if (n.isOperator("-"))
            {
                return -evaluateToDouble(n.getOperand(), parseErrors);
            }
            parseErrors.add(new ParseError("Invalid operator",n.getLexeme()));
            return 0.0;
        }
        parseErrors.add(new ParseError("Invalid expression"));
        return 0.0;
    }
    public static long evaluateToLong(ExpressionNode node,List<ParseError> parseErrors)
    {
        if (node instanceof ConstantNode)
        {
            ConstantNode n=(ConstantNode)node;
            if (n.getLexeme().getToken()==Token.INTEGER)
            {
                return (long)n.getLexeme().getValue();
            }
            parseErrors.add(new ParseError("Constant is not an integer.",n.getLexeme()));
            return 0;
        } 
        else if (node instanceof BinaryOperatorNode)
        {
            BinaryOperatorNode n=(BinaryOperatorNode)node;
            if (n.isOperator("+"))
            {
                long left=evaluateToLong(n.getLeftOperand(), parseErrors);
                long right=evaluateToLong(n.getRightOperand(), parseErrors);
                return left+right;
            }
            else if (n.isOperator("-"))
            {
                long left=evaluateToLong(n.getLeftOperand(), parseErrors);
                long right=evaluateToLong(n.getRightOperand(), parseErrors);
                return left-right;
            }
            else if (n.isOperator("/"))
            {
                long left=evaluateToLong(n.getLeftOperand(), parseErrors);
                long right=evaluateToLong(n.getRightOperand(), parseErrors);
                return left/right;
            }
            else if (n.isOperator("%"))
            {
                long left=evaluateToLong(n.getLeftOperand(), parseErrors);
                long right=evaluateToLong(n.getRightOperand(), parseErrors);
                return left%right;
            }
            parseErrors.add(new ParseError("Invalid operator",n.getLexeme()));
            return 0;
        }
        else if (node instanceof PrefixOperatorNode)
        {
            PrefixOperatorNode n=(PrefixOperatorNode)node;
            if (n.isOperator("+"))
            {
                return evaluateToLong(n.getOperand(), parseErrors);
            }
            else if (n.isOperator("-"))
            {
                return -evaluateToLong(n.getOperand(), parseErrors);
            }
            parseErrors.add(new ParseError("Invalid operator",n.getLexeme()));
            return 0;
        }
        parseErrors.add(new ParseError("Invalid expression"));
        return 0;
    }
    public static String evaluateToString(ExpressionNode node,List<ParseError> parseErrors)
    {
        if (node instanceof ConstantNode)
        {
            ConstantNode n=(ConstantNode)node;
            return (String)n.getLexeme().getValue();
        } 
        else if (node instanceof BinaryOperatorNode)
        {
            BinaryOperatorNode n=(BinaryOperatorNode)node;
            if (n.isOperator("+"))
            {
                StringBuilder sb=new StringBuilder();
                sb.append(evaluateToString(n.getLeftOperand(), parseErrors));
                sb.append(evaluateToString(n.getRightOperand(), parseErrors));
                return sb.toString();
            }
            parseErrors.add(new ParseError("Invalid operator",n.getLexeme()));
            return null;
        }
        parseErrors.add(new ParseError("Invalid expression"));
        return null;
    }
    public static boolean evaluateToBoolean(ExpressionNode node,List<ParseError> parseErrors)
    {
        if (node instanceof ConstantNode)
        {
            ConstantNode n=(ConstantNode)node;
            if (n.getLexeme().isKeyword("true"))
            {
                return true;
            }
            else if (n.getLexeme().isKeyword("false"))
            {
                return false;
            }
            parseErrors.add(new ParseError("Constant is not a boolean value.",n.getLexeme()));
        } 
        else if (node instanceof BinaryOperatorNode)
        {
            BinaryOperatorNode n=(BinaryOperatorNode)node;
            if (n.isOperator("||"))
            {
                boolean left=evaluateToBoolean(n.getLeftOperand(), parseErrors);
                boolean right=evaluateToBoolean(n.getRightOperand(), parseErrors);
                return left||right;
            }
            else if (n.isOperator("or"))
            {
                boolean left=evaluateToBoolean(n.getLeftOperand(), parseErrors);
                boolean right=evaluateToBoolean(n.getRightOperand(), parseErrors);
                return left||right;
            }
            else if (n.isOperator("&&"))
            {
                boolean left=evaluateToBoolean(n.getLeftOperand(), parseErrors);
                boolean right=evaluateToBoolean(n.getRightOperand(), parseErrors);
                return left&&right;
            }
            else if (n.isOperator("and"))
            {
                boolean left=evaluateToBoolean(n.getLeftOperand(), parseErrors);
                boolean right=evaluateToBoolean(n.getRightOperand(), parseErrors);
                return left&&right;
            }
            parseErrors.add(new ParseError("Invalid operator",n.getLexeme()));
            return false;
        }
        else if (node instanceof PrefixOperatorNode)
        {
            PrefixOperatorNode n=(PrefixOperatorNode)node;
            if (n.isOperator("!"))
            {
                return !evaluateToBoolean(n.getOperand(), parseErrors);
            }
            parseErrors.add(new ParseError("Invalid operator",n.getLexeme()));
            return false;
        }
        parseErrors.add(new ParseError("Invalid expression"));
        return false;
    }
    
}
