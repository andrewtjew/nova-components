package com.geneva.parsing.member;

import java.util.List;

import com.geneva.lexing3.Lexeme;
import com.geneva.parsing.Namespace;
import com.geneva.parsing.Node;

public class ClassMember extends Member

{
	final private Lexeme name;
	final private List<Member> members;
	final boolean public_;
	final private Namespace superClass;
	public ClassMember(boolean public_,Lexeme name,Namespace superClass,List<Member> members)
	{
		this.public_=public_;
		this.name=name;
		this.members=members;
		this.superClass=superClass;
	}
	public Lexeme getName()
	{
		return name;
	}
	public List<Member> getMembers()
	{
		return members;
	}
	public boolean isPublic()
	{
		return public_;
	}
	public Namespace getSuperClass()
	{
		return superClass;
	}
}
