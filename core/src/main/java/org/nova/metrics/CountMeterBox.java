package org.nova.metrics;

import java.lang.reflect.Method;

import org.nova.annotations.Description;

public class CountMeterBox extends MeterBox<CountMeter>
{
	CountMeterBox(String category, String name, Description description, CountMeter countMeter) throws Exception
	{
		super(category,name,description,countMeter);
	}

}
