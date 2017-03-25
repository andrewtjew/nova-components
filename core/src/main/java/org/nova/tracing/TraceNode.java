package org.nova.tracing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TraceNode
{
	private long totalDurationNs;
	private long totalWaitNs;
	private long count;
	HashMap<String,TraceNode> childTraces;
	TraceNode()
	{
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
	void update(long durationNs,long waitNs)
	{
		this.totalDurationNs+=durationNs;
		this.totalWaitNs+=waitNs;
		this.count++;
	}
	public long getTotalDurationNs()
	{
		return totalDurationNs;
	}
	public long getTotalWaitNs()
	{
		return totalWaitNs;
	}
	public long getCount()
	{
		return count;
	}
	public Map<String,TraceNode> getChildTraces()
	{
		return this.childTraces;
	}
	
}
