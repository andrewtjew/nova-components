package com.geneva.debug;

import java.io.PrintStream;
import java.util.List;

import com.geneva.lexing3.Lexeme;
import com.geneva.lexing3.LexerResult;
import com.geneva.parsing.Node;
import com.geneva.parsing.statement.Statement;
import com.geneva.util.Source;

public class Output
{
    
    static void printDebug(Lexeme lexeme)
    {
    	System.out.println("ParseException:token="+lexeme.getToken()+", value="+lexeme.getValue());
    	
    	String text=lexeme.getSource().getText();
    	System.out.println(text);
    	StringBuilder sb=new StringBuilder();
    	for (int i=0;i<lexeme.getStart();i++)
    	{
    		sb.append('_');
    	}
    	for (int i=lexeme.getStart();i<lexeme.getEnd();i++)
    	{
    		sb.append('^');
    	}
    	System.out.println(sb.toString());
    }
    
    static public void print(Source source,LexerResult result)
	{
		for (int i=0;i<result.getCount();i++)
		{
			Lexeme lexeme=result.getLexemes()[i];
	    	System.out.println(lexeme.getToken()+":"+lexeme.getValue());
		}
	}
	
	static public void print(Lexeme lexeme)
	{
    	System.out.println(lexeme.getToken()+":"+lexeme.getValue());
	}

	public static void printNodes(Node node)
	{
		printNodes(node,System.out);
	}	
	
	public static void print(List<Statement> statements)
	{
    	System.out.print("{}");
	}
	
	public static void printNodes(Node node,PrintStream printStream)
	{
		switch (node.getType())
		{
		case TERMINAL:
		{
			Lexeme lexeme=node.getElement().getLexeme();
			if (lexeme!=null)
			{
				printStream.print(node.getElement().getLexeme().getValue());
			}
		}
		break;
		case STATEMENT_BLOCK:
			print(node.getElement().getStatements());
			break;

		case PARAMETERS:
			printStream.print("(");
			printStream.print(node.getElement().getLexeme().getValue());
			printStream.print(")");
			break;

		case FUNCTION:
			printStream.print(node.getElement().getLexeme().getValue());
			break;
		case LOOKUP:
			printStream.print(node.getElement().getLexeme().getValue());
			break;
		}
		/*
		if (node.getChildNodes()!=null)
		{
			switch (node.getType())
			{
			case TERMINAL:
			case CONVERSION:
				break;

			case FUNCTION:
				printStream.print("(");
				break;
			case LOOKUP:
				printStream.print("[");
				break;
			case MAYBE_CONVERSION:
				printStream.print("***MAYBE_CONVERSION***");
				break;
			default:
				break;
			}
			
    		boolean needComma=false;
    		for (Node n:node.getChildNodes())
    		{
    			if (needComma==false)
    			{
    				needComma=true;
    			}
    			else
    			{
    				printStream.print(",");
    			}
  				printNodes(n,printStream);
    		}
			switch (node.getType())
			{
			case TERMINAL:
				break;
			case FUNCTION:
				printStream.print(")");
				break;
			case LOOKUP:
				printStream.print("]");  
				break;
			case CONVERSION:
				break;
			case MAYBE_CONVERSION:
				break;
			default:
				break;
			}
		}
		*/
	}
	

}
