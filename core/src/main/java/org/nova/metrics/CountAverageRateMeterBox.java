package org.nova.metrics;

import org.nova.annotations.Description;

public class CountAverageRateMeterBox extends MeterBox<ValueRateMeter>
{
	CountAverageRateMeterBox(String category, String name, Description description,ValueRateMeter meter) throws Exception 
	{
		super(category, name,description, meter);
	}

}
