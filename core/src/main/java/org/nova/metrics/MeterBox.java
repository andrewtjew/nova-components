package org.nova.metrics;

import org.nova.annotations.Description;

public class MeterBox<T>
{
	final private T meter;
	final private String name;
	final private String category;
	final private Description description;
	MeterBox(String category,String name,Description description,T meter) throws Exception
	{
		this.name=name;
		this.meter=meter;
		this.category=category;
		this.description=description;
	}
	public T getMeter()
	{
		return meter;
	}
	public String getName()
	{
		return name;
	}
	public String getCategory()
	{
		return category;
	}
	public String getDescription()
	{
		if (this.description==null)
		{
			return null;
		}
		return this.description.value();
	}
}
