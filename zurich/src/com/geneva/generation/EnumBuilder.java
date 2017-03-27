package com.geneva.generation;

import com.geneva.parsing.Unit;
import com.geneva.parsing.member.EnumMember;

public class EnumBuilder
{
	final private Unit unit;
	final private EnumMember enumMember;
	final private ClassBuilder outerClassBuilder;
	public EnumBuilder(Unit unit,EnumMember enumMember,ClassBuilder outerClassBuilder)
	{
		this.unit=unit;
		this.enumMember=enumMember;
		this.outerClassBuilder=outerClassBuilder;
	}
}
