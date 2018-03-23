package com.geneva.generation;

import com.geneva.lexing3.Lexeme;
import com.geneva.parsing.Element;

public class CompileException extends Exception
{
	final private Lexeme lexeme;
	final private Element element;
    private static final long serialVersionUID = 6397458296649316039L;
    
    public CompileException(Lexeme lexeme)
    {
    	this.lexeme=lexeme;
    	this.element=null;
    }
    public CompileException(Element element)
    {
    	this.lexeme=element.getLexeme();
    	this.element=element;
    }

    public CompileException()
    {
    	this.lexeme=null;
    	this.element=null;
    }


}
