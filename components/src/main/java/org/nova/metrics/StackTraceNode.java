package org.nova.metrics;

import java.util.HashMap;

public class StackTraceNode
{
    final StackTraceElement stackTraceElement;
    private HashMap<String,StackTraceNode> childNodes;
    private long durationNs; 
    
    StackTraceNode(StackTraceElement stackTraceElement)
    {
        this.stackTraceElement=stackTraceElement;
    }
    StackTraceNode(StackTraceElement stackTraceElement,long totalDurationNs)
    {
        this.stackTraceElement=stackTraceElement;
        this.durationNs=totalDurationNs;
    }
    void addChildNode(StackTraceNode node)
    {
        if (childNodes==null)
        {
            childNodes=new HashMap<>();
        }
        StackTraceElement element=node.getStackTraceElement();
        String key=getKey(element);
        this.childNodes.put(key, node);
    }
    void update(long durationNs,StackTraceElement[] stackTrace)
    {
        update(durationNs,stackTrace,stackTrace.length);
    }
    private String getKey(StackTraceElement element)
    {
        return element.getClassName()+'.'+element.getMethodName()+"."+element.getLineNumber();
    }
    void update(long durationNs,StackTraceElement[] stackTrace,int index)
    {
        this.durationNs+=durationNs;
        if (index==0)
        {
            return;
        }
        StackTraceElement element=stackTrace[--index];
        String key=getKey(element);
        if (this.childNodes==null)
        {
            this.childNodes=new HashMap<>();
            StackTraceNode node=new StackTraceNode(element);
            this.childNodes.put(key, node);
            node.update(durationNs, stackTrace, index);
        }
        else
        {
            StackTraceNode stackNode=this.childNodes.get(key);
            if (stackNode==null)
            {
                stackNode=new StackTraceNode(element);
                this.childNodes.put(key, stackNode);
            }            
            stackNode.update(durationNs, stackTrace, index);
        }
    }
    public long getDurationNs()
    {
        return this.durationNs;
    }
    public StackTraceElement getStackTraceElement()
    {
        return this.stackTraceElement;
    }
    public StackTraceNode[] getChildNodes()
    {
        if (this.childNodes==null)
        {
            return null;
        }
        return this.childNodes.values().toArray(new StackTraceNode[this.childNodes.size()]);
    }
}
