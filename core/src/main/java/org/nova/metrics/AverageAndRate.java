package org.nova.metrics;

public class AverageAndRate
{
	final private double average;
	final private double standardDeviation;
	final private double rate;

	public AverageAndRate(double average,double standardDeviation,double rate)
	{
		this.average=average;
		this.standardDeviation=standardDeviation;
		this.rate=rate;
	}

	public double getAverage()
	{
		return average;
	}

	public double getStandardDeviation()
	{
		return standardDeviation;
	}

	public double getRate()
	{
		return rate;
	}

	
}
