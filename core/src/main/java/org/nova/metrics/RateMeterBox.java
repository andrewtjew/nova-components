package org.nova.metrics;

import java.lang.reflect.Method;

import org.nova.annotations.Description;

public class RateMeterBox extends MeterBox<RateMeter>
{
	RateMeterBox(String category,String name,Description description,RateMeter rateMeter) throws Exception
	{
		super(category,name,description,rateMeter);
	}
}