package org.nova.metrics;

import org.nova.annotations.Description;

public class ValueRateMeterBox extends MeterBox<ValueRateMeter>
{
	ValueRateMeterBox(String category, String name, Description description,ValueRateMeter meter) throws Exception 
	{
		super(category, name,description, meter);
	}

}
