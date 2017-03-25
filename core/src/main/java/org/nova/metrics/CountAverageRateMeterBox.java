package org.nova.metrics;

import org.nova.annotations.Description;

public class CountAverageRateMeterBox extends MeterBox<CountAverageRateMeter>
{
	CountAverageRateMeterBox(String category, String name, Description description,CountAverageRateMeter meter) throws Exception 
	{
		super(category, name,description, meter);
	}

}
