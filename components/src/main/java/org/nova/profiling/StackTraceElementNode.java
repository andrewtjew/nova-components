package org.nova.profiling;

import java.util.HashMap;

public class StackTraceElementNode
{
    final private StackTraceElement stackTraceElement;
    private int count;
    private HashMap<String,StackTraceElementNode> subNodes;
    
    public StackTraceElementNode(StackTraceElement[] stackTrace,int index)
    {
        this.stackTraceElement=stackTrace[index];
        update(stackTrace,index);
    }
    public void update(StackTraceElement[] stackTrace,int index)
    {
        if (index==0)
        {
            this.count++;
            return;
        }
        StackTraceElement element=stackTrace[index-1];
        if (this.subNodes==null)
        {
            this.subNodes=new HashMap<>();
        }

        StackTraceElementNode node=this.subNodes.get(element.getMethodName());
        if (node==null)
        {
            node=new StackTraceElementNode(stackTrace,index-1);
            this.subNodes.put(element.getMethodName(), node);
        }
        else
        {
            node.update(stackTrace, index-1);
        }
    }
    
    public int getTotal()
    {
        int total=this.count;
        if (this.subNodes!=null)
        {
            for (StackTraceElementNode node:this.subNodes.values())
            {
                total+=node.getTotal();
            }
        }
        return total;
    }
    
}
