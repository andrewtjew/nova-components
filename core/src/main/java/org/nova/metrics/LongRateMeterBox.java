package org.nova.metrics;

import org.nova.annotations.Description;

public class LongRateMeterBox extends MeterBox<LongValueMeter>
{
	LongRateMeterBox(String category, String name, Description description,LongValueMeter meter) throws Exception 
	{
		super(category, name,description, meter);
	}

}
