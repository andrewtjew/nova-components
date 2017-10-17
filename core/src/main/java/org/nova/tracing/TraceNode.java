package org.nova.tracing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nova.metrics.TraceMeter;
import org.nova.metrics.TraceSample;

public class TraceNode
{
    private TraceMeter traceMeter;
	private HashMap<String,TraceNode> childTraceNodes;
	
	TraceNode()
	{
	    this.traceMeter=new TraceMeter();
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
    public void update(Trace trace)
    {
        synchronized(this)
        {
            this.traceMeter.update(trace);
        }
    }
    public void update(TraceSample sample)
    {
        synchronized(this)
        {
            this.traceMeter.update(sample);
        }
    }
	
	public TraceSample sampleTrace()
	{
        synchronized(this)
        {
            return traceMeter.sample();
        }
	}
	public void reset()
	{
        synchronized(this)
        {
            this.traceMeter=new TraceMeter();
        }
	}
}
