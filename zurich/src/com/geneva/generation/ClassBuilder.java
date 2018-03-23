package com.geneva.generation;

import java.util.HashMap;
import com.geneva.parsing.MutableNameType;
import com.geneva.parsing.Unit;
import com.geneva.parsing.member.ClassMember;
import com.geneva.parsing.member.FieldMember;
import com.geneva.parsing.member.Member;

public class ClassBuilder
{
	final private Unit unit;
	final private ClassMember classMember;
	final private ClassBuilder outerClassBuilder;
	private String fullName;
	private HashMap<String,FieldBuilder> fieldBuilders;
	private boolean generic;
	private boolean public_;
	
	public ClassBuilder(Unit unit,ClassMember classMember,ClassBuilder outerClassBuilder)
	{
		this.unit=unit;
		this.classMember=classMember;
		this.fieldBuilders=new HashMap<String, FieldBuilder>();
		this.outerClassBuilder=outerClassBuilder;
		this.public_=classMember.isPublic();
		this.generic=false;
	}
	
	public void buildTypeNames(String namespace)
	{
		if (namespace!=null)
		{
			this.fullName=namespace+this.classMember.getName().getValue();
		}
		else 
		{
			this.fullName=this.classMember.getName().getValue();
		}
		for (Member member:classMember.getMembers())
		{
			if (member instanceof FieldMember)
			{
				buildFieldName((FieldMember)member);
			}
		}
	}
	
	public void addField(String name,FieldBuilder fieldBuilder)
	{
		if (fieldBuilder.isGeneric())
		{
			this.generic=true;
		}
		this.fieldBuilders.put(name, fieldBuilder);
	}
	

	/*
	private static String buildSignature(RecordType type)
	{
		TreeMap<String, NameType> sorted=new TreeMap<String, NameType>();
	}
	
	private static String buildSignature(Type type)
	{
		if (type instanceof SimpleType)
		{
			return ((SimpleType)type).getNamespace().getValue();
		}
		else if (type instanceof RecordType)
		{
			
		}
		return null;
	}
	
	private static String buildSignature(MutableNameType mutableNameType)
	{
		return mutableNameType.getName()+":"+buildSignature(mutableNameType.getType());
	}
	
	*/
	private void buildFieldName(FieldMember fieldMember)
	{
		MutableNameType mutableNameType=fieldMember.getMutableNameTypeExpression().getMutableNameType();
	}
}
