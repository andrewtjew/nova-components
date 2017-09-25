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
	private HashMap<String,TraceNode> childTraceNodes;
	
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
		if (node.childTraceNodes!=null)
		{
			this.childTraceNodes=new HashMap<>();
			for (Entry<String, TraceNode> entry:node.childTraceNodes.entrySet())
			{
				this.childTraceNodes.put(entry.getKey(), new TraceNode(entry.getValue()));
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
	public Map<String,TraceNode> getChildTraceNodesSnapshot()
	{
        HashMap<String,TraceNode> childTraces=new HashMap<>();
	    synchronized(this)
	    {
	        if (this.childTraceNodes==null)
	        {
	            return null;
	        }
	        childTraces.putAll(this.childTraceNodes);
	    }
        return childTraces;
	}
	public TraceNode getOrCreateChildTraceNode(String category)
	{
        synchronized(this)
        {
            if (this.childTraceNodes==null)
            {
                this.childTraceNodes=new HashMap<>();
                TraceNode childNode=new TraceNode();
                this.childTraceNodes.put(category, childNode);
                return childNode;
            }
            TraceNode childNode=this.childTraceNodes.get(category);
            if (childNode==null)
            {
                childNode=new TraceNode();
                this.childTraceNodes.put(category, childNode);
            }
            return childNode;
        }
	    
	    
	}
	public TraceNodeSample sample()
	{
	    synchronized(this)
	    {
	        return new TraceNodeSample(this.totalDurationNs,this.totalWaitNs,this.maxDurationNs,this.maxWaitNs,this.maxDurationInstantMs,this.maxWaitInstantMs,this.minDurationNs,this.minWaitNs,this.minDurationInstantMs,this.minWaitInstantMs,this.count,this.exceptionCount,this.lastExceptionTrace);
	    }
	}
	
}
