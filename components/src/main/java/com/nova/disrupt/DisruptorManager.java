package com.nova.disrupt;

public class DisruptorManager
{
    
    public Disruptor getDisruptor(String key)
    {
        return new Disruptor(null,null,null);
    }
}
