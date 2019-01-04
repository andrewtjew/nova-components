package org.nova.metrics;

public class ThreadExecutionSample
{
    final private StackTraceNode root;
    final private boolean ended;
    
    public ThreadExecutionSample(boolean ended,StackTraceNode root)
    {
        this.ended=ended;
        this.root=copy(root);
    }
    
    private StackTraceNode copy(StackTraceNode node)
    {
        StackTraceNode copy=new StackTraceNode(node.getStackTraceElement(),node.getActiveNs(),node.getWaitNs(),node.getCount());
        if (node.getChildNodes()!=null)
        {
            for (StackTraceNode childNode:node.getChildNodes())
            {
                copy.addChildNode(copy(childNode));
            }
        }
        return copy;
    }
    public boolean isEnded()
    {
        return this.ended;
    }
    public StackTraceNode getRoot()
    {
        return this.root;
    }
}
