package org.nova.logging;

import org.nova.flow.SourceQueueConfiguration;

public class JSONBufferedLZ4QueueConfiguration extends SourceQueueConfiguration
{
    public long writerStallWait=100;
    
    //For best performance set writerMaxQueueSize=2*writerThreads*writerSegmentSize, otherwise we will stall
    //writerStallSizeThreshold = writerMaxQueueSize- writerSegmentSize; 
    
    public int writerStallSizeThreshold=0;  //writerStallSizeThreshold = writerMaxQueueSize- writerSegmentSize;
    public int writerMaxQueueSize      =0;  //writerMaxQueueSize=2*writerThreads*writerSegmentSize
    public int writerSegmentSize      = 100000;
    public int writerBufferInitialCapacity=65536;
    public int writerThreads=4;

    public int entriesPerFile=20000;
	public int samplingInterval=10000;
	
	public JSONBufferedLZ4QueueConfiguration(int entriesPerFile)
	{
		this.entriesPerFile=entriesPerFile;
		this.rollOverWait=200; //Since the log entries are buffered, we need to set rollover to small value to prevent data loss. 
	}
	public JSONBufferedLZ4QueueConfiguration()
	{
	}
}
