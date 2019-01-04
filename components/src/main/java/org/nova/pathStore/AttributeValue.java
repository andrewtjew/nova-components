package org.nova.pathStore;

import org.nova.utils.Utils;

public class AttributeValue<A,V> extends Node
{
	final private V value;
    final private A attribute;
	final private String[] pathElements;

	public AttributeValue(String[] pathElements,A attribute,V value)
	{
        this.attribute=attribute;
		this.value=value;
		this.pathElements=pathElements;
	}
	public V get()
	{
		return value;
	}
	public A getAttribute()
	{
	    return this.attribute;
	}
	public String getPath()
	{
		return '/'+Utils.combine(this.pathElements, "/");
	}
	public String[] getPathElements()
	{
	    return this.pathElements;
	}
}
