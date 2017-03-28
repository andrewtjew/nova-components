package com.geneva.parsing.type;

import java.util.List;

public class AlternativeType extends Type
{
	private final List<Type> alternatives;
	public AlternativeType(List<Type> alternatives)
	{
		this.alternatives=alternatives;
	}
	public List<Type> getAlternatives()
	{
		return alternatives;
	}
	
}
