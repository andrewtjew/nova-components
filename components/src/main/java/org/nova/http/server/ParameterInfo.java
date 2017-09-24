package org.nova.http.server;

import java.lang.annotation.Annotation;

public class ParameterInfo
{
	final private ParameterSource source;
	final private int index;
	final private Class<?> type;
	final private Object defaultValue;
	final private int pathIndex;
	final private String name;
	final private Annotation annotation;
	public ParameterInfo(ParameterSource source,Annotation annotation,String name,int index,Class<?> type,Object defaultValue)
	{
		this.source=source;
        this.annotation=annotation;
		this.name=name;
		this.index=index;
		this.type=type;
		this.defaultValue=defaultValue;
		this.pathIndex=0;
	}
	public ParameterInfo(ParameterInfo info,int pathIndex)
	{
		this.source=info.source;
        this.annotation=info.annotation;
		this.name=info.name;
		this.index=info.index;
		this.type=info.type;
		this.defaultValue=info.defaultValue;
		this.pathIndex=pathIndex;
	}
	public ParameterSource getSource()
	{
		return source;
	}
	public int getIndex()
	{
		return index;
	}
	public Class<?> getType()
	{
		return type;
	}
	public Object getDefaultValue()
	{
		return defaultValue;
	}
	public int getPathIndex()
	{
		return pathIndex;
	}
	public String getName()
	{
		return name;
	}
	public Class<?> getParameterType()
	{
	    return this.type;
	}
	public Annotation getAnnotation()
	{
	    return this.annotation;
	}
}
