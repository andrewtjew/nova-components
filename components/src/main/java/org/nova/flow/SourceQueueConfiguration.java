package org.nova.flow;

public class SourceQueueConfiguration
{
    public long flushWaitMs=200;
    public int sendSizeThreshold=100;
	public long sendWaitMs=100;
	public int stallSizeThreshold=10000;
	public long stallWaitMs=500;
	public long rollOverWaitMs=5*60*1000; 
	public int maxQueueSize=20000;
	
	public SourceQueueConfiguration(int maxSize)
	{
		this.maxQueueSize=maxSize;
	}
	public SourceQueueConfiguration()
	{
	}
}
