package org.nova.metrics;

import org.nova.annotations.Description;

public class LevelMeterBox extends MeterBox<LevelMeter>
{
	LevelMeterBox(String category, String name,Description description, LevelMeter meter) throws Exception
	{
		super(category, name, description,meter);
	}

}
