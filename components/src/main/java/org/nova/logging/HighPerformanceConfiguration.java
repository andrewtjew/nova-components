package org.nova.logging;

import org.nova.flow.SourceQueueConfiguration;

public class HighPerformanceConfiguration extends SourceQueueConfiguration
{
    public long writerStallWaitMs=100;
    
    //For best performance set writerMaxQueueSize=2*writerThreads*writerSegmentSize, otherwise we will stall
    //writerStallSizeThreshold = writerMaxQueueSize- writerSegmentSize; 
    
    public int writerStallSizeThreshold=0;  //set to zero to compute writerStallSizeThreshold = writerMaxQueueSize- writerSegmentSize;
    public int writerMaxQueueSize      =0;  //set to zero to compute writerMaxQueueSize=2*writerThreads*writerSegmentSize
    public int writerSegmentSize      = 100000;
    public int writerBufferInitialCapacity=65536;
    public int writerThreads=4;

    public int entriesPerFile=20000;
	
	public HighPerformanceConfiguration(int entriesPerFile)
	{
		this.entriesPerFile=entriesPerFile;
		this.rollOverWaitMs=200; //Since the log entries are buffered, we need to set rollover to small value to prevent data loss. 
	}
	public HighPerformanceConfiguration()
	{
	}
}
