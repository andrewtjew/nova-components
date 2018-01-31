package org.nova.tracing;

import org.nova.metrics.TraceSample;

public class CategorySample
{
	final TraceSample sample;
	final String category;
	public CategorySample(String category,TraceSample sample)
	{
		this.category=category;
		this.sample=sample;
	}
	public String getCategory()
	{
		return category;
	}
	public TraceSample getSample()
	{
	    return this.sample;
	}
	
	
}
