package com.geneva.util;

import com.geneva.debug.Debug;
import com.geneva.lexing3.Lexeme;
import com.geneva.parsing.Element;

public class ParseException extends Exception
{
	final private Lexeme lexeme;
	final private Element element;
    private static final long serialVersionUID = 6397458296649316039L;
    
    
    static void printDebug(Lexeme lexeme)
    {
    	/*
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
    	*/
    	System.out.println("ParseException:token="+lexeme.getToken()+", value="+lexeme.getValue());
    	int begin=lexeme.getStart()-1;
    	String text=lexeme.getSource().getText();
    	for (;begin>=0&&text.charAt(begin)!='\n';begin--)
    	{
    	}
    	int end=lexeme.getStart()+1;
    	for (;end<text.length()&&text.charAt(end)!='\n'&&text.charAt(end)!='\r';end++)
    	{
    	}
    	begin+=1;
    	String line=text.substring(begin,end);
    	System.out.println(line);
    	StringBuilder sb=new StringBuilder();
    	for (int i=begin;i<lexeme.getStart();i++)
    	{
    		if (text.charAt(i)=='\t')
    		{
    			sb.append("____");
    			
    		}
    		else
			{
    			sb.append('_');
			}
    	}
    	for (int i=lexeme.getStart();i<lexeme.getEnd();i++)
    	{
    		sb.append('^');
    	}
    	System.out.println(sb.toString());
    	
    	
    }
    
    
    public ParseException(Lexeme lexeme)
    {
    	if (Debug.DEBUG)
    	{
  			printDebug(lexeme);
    	}
    	this.lexeme=lexeme;
    	this.element=null;
    }
    public ParseException(Element element)
    {
    	if (Debug.DEBUG)
    	{
    		if (element.getLexeme()!=null)
  			printDebug(element.getLexeme());
    	}
    	this.lexeme=element.getLexeme();
    	this.element=element;
    }

    public ParseException()
    {
    	this.lexeme=null;
    	this.element=null;
    }



}
