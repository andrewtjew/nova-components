package org.nova.tracing;

import org.nova.metrics.ValueRateMeter;
import org.nova.metrics.ValueRateSample;

public class TraceSample
{
	final ValueRateSample sample;
	final String category;
	public TraceSample(String category,ValueRateSample sample)
	{
		this.category=category;
		this.sample=sample;
	}
	public String getCategory()
	{
		return category;
	}
	public ValueRateSample getSample()
	{
	    return this.sample;
	}
	
	
}
