package com.geneva.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.lang.model.type.PrimitiveType;

import com.geneva.debug.Debug;
import com.geneva.parsing.MutableNameType;
import com.geneva.parsing.NameType;
import com.geneva.parsing.Namespace;
import com.geneva.parsing.Unit;
import com.geneva.parsing.member.ClassMember;
import com.geneva.parsing.member.EnumMember;
import com.geneva.parsing.member.FieldMember;
import com.geneva.parsing.member.FunctionMember;
import com.geneva.parsing.member.Member;
import com.geneva.parsing.member.NamespaceMember;
import com.geneva.parsing.member.UsingMember;
import com.geneva.parsing.type.AlternativeType;
import com.geneva.parsing.type.FunctionType;
import com.geneva.parsing.type.RecordType;
import com.geneva.parsing.type.SimpleType;
import com.geneva.parsing.type.Type;

public class UnitBuilder
{
//	private HashMap<String,ClassBuilder> classBuilders;
//	private HashMap<String,EnumBuilder> enumBuilders;
//	private HashMap<String,EnumBuilder> enumBuilders;
	
	final private Unit unit;
	final private Builder builder;
	
	private HashSet<String> usings;
	private HashMap<String,String> aliases;
	
	public UnitBuilder(Builder builder,Unit unit)
	{
		this.builder=builder;
		this.unit=unit;
	}
	

	private static String buildFullName(String parentNameSpace,String name)
	{
		if (parentNameSpace!=null)
		{
			return parentNameSpace+"."+name;
		}
		return name;
	}
	
	public void buildSpecifiers() throws CompileException
	{
		this.aliases=new HashMap<String, String>();
		this.usings=new HashSet<String>();
		for (Member member:unit.getMembers())
		{
			buildSpecifiers(null,member);
		}	
	}
	
	private void buildSpecifiers(String parentNamespace,Member member) throws CompileException
	{
		if (member instanceof NamespaceMember)
		{
			NamespaceMember m=(NamespaceMember)member;
			String namespace=buildFullName(parentNamespace, m.getNamespace().getValue());
			this.usings.add(namespace);
			for (Member mm:m.getMembers())
			{
				buildSpecifiers(namespace,mm);
			}
		}
		else if (member instanceof UsingMember)
		{
			UsingMember m=(UsingMember)member;
			if (m.getAlias()!=null)
			{
				this.aliases.put(m.getAlias().getValue(), m.getNamespace().getValue());
			}
			else
			{
				this.usings.add(m.getNamespace().getValue());
			}
		}
		else if (member instanceof ClassMember)
		{
			buildSpecifiers(parentNamespace, (ClassMember)member);
		}
		else if (member instanceof EnumMember)
		{
//			buildEnumBuilder(parentNamespace, (EnumMember)member, null);
		}
		else if (member instanceof FieldMember)
		{
			
		}	
	}
	
	private void buildSpecifiers(String parentNamespace,ClassMember member) throws CompileException
	{
		String fullName=buildFullName(parentNamespace, member.getName().getValue());
		ClassBuilder classBuilder=this.builder.getClassBuilder(fullName);
		for (Member m:member.getMembers())
		{
			if (m instanceof ClassMember)
			{
				buildSpecifiers(fullName,(ClassMember)m);
			}
			else if (m instanceof FunctionMember)
			{
				//checkTypeNames(fullName,(EnumMember)m);
			}
			else if (m instanceof FieldMember)
			{
				buildFieldBuilder(classBuilder,(FieldMember)m);
			}
		}
	}	
	private void buildFieldBuilder(ClassBuilder classBuilder,FieldMember member) throws CompileException
	{
		FieldBuilder fieldBuilder=new FieldBuilder(member);
		Type type=member.getMutableNameTypeExpression().getMutableNameType().getType();
		if (type!=null)
		{
			fieldBuilder.setTypeSpecifierBuilder(buildTypeSpecifierBuilder(type));
		}
		else
		{
			fieldBuilder.setTypeSpecifierBuilder(null);
		}
		classBuilder.addField(member.getMutableNameTypeExpression().getMutableNameType().getName().getValue(), fieldBuilder);
		
	}
	
	private TypeSpecifierBuilder buildTypeSpecifierBuilder(Type type) throws CompileException
	{
		TypeSpecifierBuilder builder=new TypeSpecifierBuilder(type);
		String fullName=build(builder,type);
		if (fullName==null)
		{
			throw new CompileException(); //
		}
		builder.setFullName(fullName);
		return builder;
	}

	private String build(TypeSpecifierBuilder builder,Type type) throws CompileException
	{
		if (type instanceof SimpleType)
		{
			SimpleType t=(SimpleType)type;
			String name=resolveAlias(t.getNamespace());
			if (isTypeDeclared(name))
			{
				return name;
			}
			for (String using:this.usings)
			{
				String namespace=using+"."+name;
				if (isTypeDeclared(namespace))
				{
					return namespace;
				}
			}
			return null;
		}
		else if (type instanceof AlternativeType)
		{
			AlternativeType t=(AlternativeType)type;
			TreeMap<String,Type> map=new TreeMap<String, Type>();
			for (Type item:t.getAlternatives())
			{
				String fullName=build(builder,item);
				if (fullName==null)
				{
					return null;
				}
				if (map.containsKey(fullName))
				{
					throw new CompileException(); //A type can only be specified once
				}
				map.put(fullName,item);
			}
			StringBuilder sb=new StringBuilder();
			sb.append("@alt");
			for (String fullName:map.keySet())
			{
				sb.append('+');
				sb.append(fullName);
			}
			return sb.toString();
		}
		else if (type instanceof RecordType)
		{
			RecordType t=(RecordType)type;
			TreeMap<String,String> map=new TreeMap<String, String>();
			for (NameType item:t.getNameTypes())
			{
				String name=item.getName().getValue();
				if (map.containsKey(name)==true)
				{
					throw new CompileException(); //todo: cannot have duplicate names
				}
				Type itemType=item.getType();
				String value=null;
				if (itemType!=null)
				{
					builder.setGenericToTrue();
					value=build(builder,itemType);
				}
				map.put(name, value);
			}
			StringBuilder sb=new StringBuilder();
			sb.append("@rec");
			for (Entry<String,String> entry:map.entrySet())
			{
				sb.append('+');
				sb.append(entry.getKey());
				if (entry.getValue()!=null)
				{
					sb.append(':');
					sb.append(entry.getValue());
				}
			}
			return sb.toString();
		}
		else if (type instanceof FunctionType)
		{
			//todo
		}
		throw new RuntimeException(); //todo: remove
	}
	
	private boolean isValidType(Type type) throws CompileException
	{
		if (type instanceof SimpleType)
		{
			SimpleType t=(SimpleType)type;
			String name=resolveAlias(t.getNamespace());
			if (isTypeDeclared(name))
			{
				return true;
			}
			for (String using:this.usings)
			{
				String namespace=using+"."+name;
				if (isTypeDeclared(namespace))
				{
					return true;
				}
			}
			return false;
		}
		else if (type instanceof AlternativeType)
		{
			AlternativeType t=(AlternativeType)type;
			for (Type item:t.getAlternatives())
			{
				//Todo: make sure alternatives are not the same type.
				if (isValidType(item)==false)
				{
					return false;
				}
			}
			return true;
		}
		else if (type instanceof RecordType)
		{
			RecordType t=(RecordType)type;
			HashSet<String> names=new HashSet<String>();
			for (NameType item:t.getNameTypes())
			{
				Type itemType=item.getType();
				String name=item.getName().getValue();
				if (names.contains(name)==true)
				{
					throw new CompileException(); //todo: cannot have duplicate names
				}
				names.add(name);
				if (itemType!=null)
				{
    				if (isValidType(itemType)==false)
    				{
    					return false;
    				}
				}
				else
				{
					//Todo: generic
				}
			}
			return true;
			
		}
		else if (type instanceof FunctionType)
		{
			//todo
		}
		throw new RuntimeException(); //todo: remove
	}
	
	
	
	public void buildTypeNames() throws CompileException
	{
		for (Member member:unit.getMembers())
		{
			buildTypeNames(null,member,null);
		}
	}
	
	private void buildClassBuilder(String parentNameSpace,ClassMember member,ClassBuilder outerClassBuilder) throws CompileException
	{
		String namespace=buildFullName(parentNameSpace, member.getName().getValue());
		ClassBuilder builder=new ClassBuilder(this.unit,member,outerClassBuilder);
		if (this.builder.tryAdd(namespace, builder)==false)
		{
			throw new CompileException();
		}
		if (Debug.DEBUG)
		{
			if (Debug.printBuildNames)
			{
				System.out.println("class "+namespace);
			}
		}
		for (Member m:member.getMembers())
		{
			if (m instanceof ClassMember)
			{
				buildClassBuilder(namespace,(ClassMember)m,builder);
			}
			else if (m instanceof EnumMember)
			{
				buildEnumBuilder(namespace,(EnumMember)m,builder);
			}
		}
	}
	private void buildEnumBuilder(String parentNameSpace,EnumMember member,ClassBuilder outerClassBuilder) throws CompileException
	{
		String namespace=buildFullName(parentNameSpace, member.getName().getValue());
		EnumBuilder builder=new EnumBuilder(this.unit,member,outerClassBuilder);
		if (this.builder.tryAdd(namespace, builder)==false)
		{
			throw new CompileException();
		}
		if (Debug.DEBUG)
		{
			if (Debug.printBuildNames)
			{
				System.out.println("class "+namespace);
			}
		}
	}
	
	private void buildTypeNames(String parentNameSpace,Member member,ClassBuilder outerClassBuilder) throws CompileException
	{
		if (member instanceof NamespaceMember)
		{
			NamespaceMember m=(NamespaceMember)member;
			String namespace=buildFullName(parentNameSpace, m.getNamespace().getValue());
			for (Member mm:m.getMembers())
			{
				buildTypeNames(namespace,mm,null);
			}
		}
		else if (member instanceof ClassMember)
		{
			buildClassBuilder(parentNameSpace, (ClassMember)member, null);
		}
		else if (member instanceof EnumMember)
		{
			buildEnumBuilder(parentNameSpace, (EnumMember)member, null);
		}
		else if (member instanceof FieldMember)
		{
			
		}
	}
	private boolean isTypeDeclared(String name)
	{
		if (this.builder.isPrimitive(name))
		{
			return true;
		}
		if (this.builder.getClassBuilder(name)!=null)
		{
			return true;
		}
		if (this.builder.getEnumBuilder(name)!=null)
		{
			return true;
		}
		return false;
	}
	private String resolveAlias(Namespace namespace)
	{
		if (namespace.getLexemes().size()==1)
		{
			return namespace.getValue();
		}
		String alias=namespace.getLexemes().get(0).getValue();
		String path=this.aliases.get(alias);
		if (path==null)
		{
			return namespace.getValue();
		}
		return path+namespace.getValue().substring(alias.length());
	}
	
}
