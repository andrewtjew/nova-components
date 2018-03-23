package com.geneva.parsing.member;

import java.util.List;

import com.geneva.lexing3.Lexeme;
import com.geneva.parsing.Modifiers;
import com.geneva.parsing.MutableNameType;
import com.geneva.parsing.NameTypeExpression;
import com.geneva.parsing.statement.Statement;
import com.geneva.parsing.type.Type;

public class FunctionMember extends Member
{
	final private Lexeme name;
	final private List<Statement> statements;
	final private Modifiers modifiers;
	final private List<NameTypeExpression> attributes;
	final private List<MutableNameType> inputParameters;
//	final private List<NameType> outputParameters;
	final private Type outputType;
	public FunctionMember(List<NameTypeExpression> attributes,Modifiers modifiers,Lexeme name,List<MutableNameType> inputParameters,Type outputType,List<Statement> statements)
	{
		this.attributes=attributes;
		this.name=name;
		this.inputParameters=inputParameters;
		this.outputType=outputType;
//		this.outputParameters=outputParameters;
		this.statements=statements;
		this.modifiers=modifiers;
	}
	public Lexeme getName()
	{
		return name;
	}
	public Modifiers getModifiers()
	{
		return this.modifiers;
	}
	public List<Statement> getStatements()
	{
		return statements;
	}
	public List<NameTypeExpression> getAttributes()
	{
		return this.attributes;
	}
	public List<MutableNameType> getInputParameters()
	{
		return inputParameters;
	}
	public Type getOutputType()
	{
		return outputType;
	}
	
	/*
	public List<NameType> getOutputParameters()
	{
		return outputParameters;
	}
	*/

}
