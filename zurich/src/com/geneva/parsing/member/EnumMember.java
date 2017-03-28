package com.geneva.parsing.member;

import com.geneva.lexing3.Lexeme;
import com.geneva.parsing.Node;

public class EnumMember extends Member
{
	final private Lexeme name;
	final private Node type;
	final private Node elements;
	final boolean public_;
	
	public EnumMember(boolean public_,Lexeme name,Node type,Node elements)
	{
		this.public_=public_;
		this.type=type;
		this.name=name;
		this.elements=elements;
	}
	public Lexeme getName()
	{
		return name;
	}
	public Node getElements()
	{
		return elements;
	}
	public boolean isPublic()
	{
		return public_;
	}
	public Node getType()
	{
		return type;
	}
}
