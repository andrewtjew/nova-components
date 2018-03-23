package com.geneva.parsing.type;

import java.util.List;

import com.geneva.parsing.MutableNameType;

public class FunctionType extends Type
{
	final private List<MutableNameType> inputTypes;
	final private Type outputType;
	public FunctionType(List<MutableNameType> inputTypes,Type outputType)
	{
		this.inputTypes=inputTypes;
		this.outputType=outputType;
	}
	public List<MutableNameType> getInputTypes()
	{
		return inputTypes;
	}
	public Type getOutputType()
	{
		return outputType;
	}
	

}
