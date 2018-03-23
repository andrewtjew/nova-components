package com.geneva.generation;

import java.util.HashMap;

public class Builder
{
	private HashMap<String,ClassBuilder> classBuilders;
	private HashMap<String,EnumBuilder> enumBuilders;
	private HashMap<String,Primitive> primitives;
	
	public Builder()
	{
		this.classBuilders=new HashMap<String, ClassBuilder>();
		this.enumBuilders=new HashMap<String, EnumBuilder>();
		this.primitives=new HashMap<String, Primitive>();
		this.primitives.put("string", Primitive.STRING);
		this.primitives.put("System.String", Primitive.STRING);
		this.primitives.put("System.Integer", Primitive.INTEGER);
		this.primitives.put("int", Primitive.INTEGER);
	}

	public HashMap<String, ClassBuilder> getClassBuilders()
	{
		return classBuilders;
	}

	public HashMap<String, EnumBuilder> getEnumBuilders()
	{
		return enumBuilders;
	}

	public boolean tryAdd(String fullName,ClassBuilder builder)
	{
		synchronized(this.classBuilders)
		{
			if (this.classBuilders.containsKey(fullName))
			{
				return false;
			}
			this.classBuilders.put(fullName, builder);
			return true;
		}
	}
	public boolean tryAdd(String fullName,EnumBuilder builder)
	{
		synchronized(this.enumBuilders)
		{
			if (this.enumBuilders.containsKey(fullName))
			{
				return false;
			}
			this.enumBuilders.put(fullName, builder);
			return true;
		}
	}
	public ClassBuilder getClassBuilder(String fullName)
	{
		synchronized(this.classBuilders)
		{
			return this.classBuilders.get(fullName);
		}
	}
	public EnumBuilder getEnumBuilder(String fullName)
	{
		synchronized(this.enumBuilders)
		{
			return this.enumBuilders.get(fullName);
		}
	}
	
	public boolean isPrimitive(String name)
	{
		return this.primitives.containsKey(name);
	}
	
}
