package com.geneva.generation;

import com.geneva.parsing.Unit;
import com.geneva.parsing.member.FieldMember;

public class FieldBuilder
{
	final private FieldMember fieldMember;
	private TypeSpecifierBuilder typeSpecifierBuilder;
	private boolean generic;
	
	public FieldBuilder(FieldMember fieldMember)
	{
		this.fieldMember=fieldMember;
		this.generic=false;
	}

	public TypeSpecifierBuilder getTypeSpecifierBuilder()
	{
		return typeSpecifierBuilder;
	}

	public void setTypeSpecifierBuilder(TypeSpecifierBuilder typeSpecifierBuilder)
	{
		this.typeSpecifierBuilder = typeSpecifierBuilder;
		if (typeSpecifierBuilder==null)
		{
			this.generic=true;
		}
	}

	public FieldMember getFieldMember()
	{
		return fieldMember;
	}

	public boolean isGeneric()
	{
		return generic;
	}

}
