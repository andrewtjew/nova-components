/*******************************************************************************
 * Copyright (C) 2016-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.tracing;

import java.util.HashMap;
import java.util.Map;

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
