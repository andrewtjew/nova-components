package org.nova.metrics;

public class Rates
{
    final private double currentRate;
    final private double lastRate;
    final private double lastLastRate;
    
    public Rates(double currentRate,double lastRate,double lastLastRate)
    {
        this.currentRate=currentRate;
        this.lastRate=lastRate;
        this.lastLastRate=lastLastRate;
    }

    public double getCurrentRate()
    {
        return currentRate;
    }

    public double getLastRate()
    {
        return lastRate;
    }

    public double getLastLastRate()
    {
        return lastLastRate;
    }
    
}
