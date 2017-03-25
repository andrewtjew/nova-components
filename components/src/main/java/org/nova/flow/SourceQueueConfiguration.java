package org.nova.flow;

public class SourceQueueConfiguration
{
    public long flushWait=200;
    public int sendSizeThreshold=100;
	public long sendWait=100;
	public int stallSizeThreshold=10000;
	public long stallWait=500;
	public long rollOverWait=10*1000; 
	public int maxQueueSize=20000;
	
	public SourceQueueConfiguration(int maxSize)
	{
		this.maxQueueSize=maxSize;
	}
	public SourceQueueConfiguration()
	{
	}
}
