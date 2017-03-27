package com.geneva.parsing.member;

import java.util.List;

import com.geneva.parsing.Modifiers;
import com.geneva.parsing.MutableNameTypeExpression;
import com.geneva.parsing.NameTypeExpression;

public class FieldMember extends Member
{
	private final Modifiers modifiers;
	private final List<NameTypeExpression> attributes;
	private final MutableNameTypeExpression mutableNameTypeExpression;
	public FieldMember(List<NameTypeExpression> attributes,Modifiers modifiers,MutableNameTypeExpression mutableNameTypeExpression)
	{
		this.modifiers=modifiers;
		this.attributes=attributes;
		this.mutableNameTypeExpression=mutableNameTypeExpression;
	}
	public Modifiers getModifiers()
	{
		return modifiers;
	}
	public List<NameTypeExpression> getAttributes()
	{
		return attributes;
	}
	public MutableNameTypeExpression getMutableNameTypeExpression()
	{
		return mutableNameTypeExpression;
	}

}
