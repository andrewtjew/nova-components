package org.nova.metrics;

public class RateResult
{
    final private double currentRate;
    final private double lastRate;
    
    public RateResult(double currentRate,double lastRate)
    {
        this.currentRate=currentRate;
        this.lastRate=lastRate;
    }

    public double getCurrentRate()
    {
        return currentRate;
    }

    public double getLastRate()
    {
        return lastRate;
    }

}
