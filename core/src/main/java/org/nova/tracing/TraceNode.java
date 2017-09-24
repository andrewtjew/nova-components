package org.nova.tracing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TraceNode
{
    private long totalDurationNs;
    private long totalWaitNs;
    private long maxDurationNs;
    private long maxWaitNs;
    private long maxDurationInstantMs;
    private long maxWaitInstantMs;
    private long minDurationNs;
    private long minWaitNs;
    private long minDurationInstantMs;
    private long minWaitInstantMs;
	private long count;
	private Trace lastExceptionTrace;
	private long exceptionCount;
	HashMap<String,TraceNode> childTraces;
	
	TraceNode()
	{
        this.maxDurationNs=Long.MIN_VALUE;
        this.maxWaitNs=Long.MIN_VALUE;
        this.minDurationNs=Long.MAX_VALUE;
        this.minWaitNs=Long.MAX_VALUE;
	}
	TraceNode(TraceNode node)
	{
		this.totalDurationNs=node.totalDurationNs;
		this.totalWaitNs=node.totalWaitNs;
		this.count=node.count;
		if (node.childTraces!=null)
		{
			this.childTraces=new HashMap<>();
			for (Entry<String, TraceNode> entry:node.childTraces.entrySet())
			{
				this.childTraces.put(entry.getKey(), new TraceNode(entry.getValue()));
			}
		}
	}
	void update(Trace trace)
	{
	    long durationNs=trace.getDurationNs();
	    long waitNs=trace.getWaitNs();
        Throwable throwable=trace.getThrowable();
	    synchronized(this)
	    {
	        if (throwable!=null)
	        {
	            this.lastExceptionTrace=trace;
	            this.exceptionCount++;
	        }
            if (durationNs>=this.maxDurationNs)
            {
                this.maxDurationNs=durationNs;
                this.maxDurationInstantMs=System.currentTimeMillis();
            }
            if (durationNs<=this.minDurationNs)
            {
                this.minDurationNs=durationNs;
                this.minDurationInstantMs=System.currentTimeMillis();
            }
            if (waitNs>=this.maxWaitNs)
            {
                this.maxWaitNs=waitNs;
                this.maxWaitInstantMs=System.currentTimeMillis();
            }
            if (durationNs<=this.minWaitNs)
            {
                this.minWaitNs=durationNs;
                this.minWaitInstantMs=System.currentTimeMillis();
            }
    		this.totalDurationNs+=durationNs;
    		this.totalWaitNs+=waitNs;
    		this.count++;
	    }
	}
	public Map<String,TraceNode> getChildTraces()
	{
		return this.childTraces;
	}
	public TraceNodeSample sample()
	{
	    synchronized(this)
	    {
	        return new TraceNodeSample(this.totalDurationNs,this.totalWaitNs,this.maxDurationNs,this.maxWaitNs,this.maxDurationInstantMs,this.maxWaitInstantMs,this.minDurationNs,this.minWaitNs,this.minDurationInstantMs,this.minWaitInstantMs,this.count,this.exceptionCount,this.lastExceptionTrace);
	    }
	}
	
}
