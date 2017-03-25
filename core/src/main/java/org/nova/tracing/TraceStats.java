package org.nova.tracing;

import org.nova.metrics.CountAverageRateMeter;

public class TraceStats
{
	final CountAverageRateMeter meter;
	final String category;
	public TraceStats(String category)
	{
		this.category=category;
		this.meter=new CountAverageRateMeter();
	}
	void update(long value)
	{
		this.meter.update(value);
	}
	public CountAverageRateMeter getMeter()
	{
		return meter;
	}
	public String getCategory()
	{
		return category;
	}
	
	
}
