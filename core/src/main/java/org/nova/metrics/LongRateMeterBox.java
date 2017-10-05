package org.nova.metrics;

import org.nova.annotations.Description;

public class LongRateMeterBox extends MeterBox<LongRateMeter>
{
	LongRateMeterBox(String category, String name, Description description,LongRateMeter meter) throws Exception 
	{
		super(category, name,description, meter);
	}

}
