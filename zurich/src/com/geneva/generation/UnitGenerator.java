package com.geneva.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.geneva.parsing.Unit;
import com.geneva.parsing.member.ClassMember;
import com.geneva.parsing.member.EnumMember;
import com.geneva.parsing.member.FieldMember;
import com.geneva.parsing.member.FunctionMember;
import com.geneva.parsing.member.Member;
import com.geneva.parsing.member.NamespaceMember;
import com.geneva.parsing.member.UsingMember;

public class UnitGenerator
{
	private ArrayList<String> usings;
	private HashMap<String,String> aliases;
	HashMap<String,ClassBuilder> classes;
	HashMap<String,EnumBuilder> enums;
	
	private String namespace=null;
	
	public UnitGenerator()
	{
		this.usings=new ArrayList<String>();
		this.aliases=new HashMap<String, String>();
	}

	
	
	public void generate(Unit unit) throws CompileException
	{
		generateMembers(unit.getMembers());
	}
	
	private void generateMembers(List<Member> members) throws CompileException
	{
    	for (Member member:members)
    	{
			generate(member);
    	}
	}

	private void generate(Member member) throws CompileException
	{
		if (member instanceof UsingMember)
		{
			generateUsingMember((UsingMember)member);
		}
		else if (member instanceof NamespaceMember)
		{
			generateNamespace((NamespaceMember)member);
		}
		else if (member instanceof ClassMember)
		{
			generateClassMember((ClassMember)member);
		}
		else if (member instanceof EnumMember)
		{
			generateEnumMember((EnumMember)member);
		}
		else if (member instanceof ClassMember)
		{
			generateFieldMember((FieldMember)member);
		}
		else if (member instanceof FunctionMember)
		{
			generateFunctionMember((FunctionMember)member);
		}
	}
	
	private void generateUsingMember(UsingMember member) throws CompileException
	{
		if (member.getAlias()!=null)
		{
			String alias=member.getAlias().getValue();
			if (this.aliases.containsKey(alias))
			{
				throw new CompileException(); 
			}
			this.aliases.put(alias,member.getNamespace().getValue());
		}
		else
		{
			this.usings.add(member.getNamespace().getValue());
		}
	}
	private void generateNamespace(NamespaceMember member) throws CompileException
	{
		this.namespace=member.getNamespace().getValue();
		generateMembers(member.getMembers());
	}
	
	private void generateClassMember(ClassMember member) throws CompileException
	{
	}
	private void generateEnumMember(EnumMember member) throws CompileException
	{
	}
	private void generateFieldMember(FieldMember member) throws CompileException
	{
	}
	private void generateFunctionMember(FunctionMember member) throws CompileException
	{
	}
}
