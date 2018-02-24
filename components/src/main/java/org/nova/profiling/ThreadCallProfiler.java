package org.nova.profiling;

public class ThreadCallProfiler
{
    final private Thread thread;
    private StackTraceElementNode root;
    
    public ThreadCallProfiler(Thread thread)
    {
        this.thread=thread;
    }
    
    public void sample(int samples,long waitBetweenSamplingMs)
    {
        for (int i=0;i<samples;i++)
        {
            StackTraceElement[] stackTrace=thread.getStackTrace();
            if (this.root==null)
            {
                this.root=new StackTraceElementNode(stackTrace, stackTrace.length-1);
            }
            else
            {
                this.root.update(stackTrace,stackTrace.length-1);
            }
            if (waitBetweenSamplingMs>0)
            {
                try
                {
                    Thread.sleep(waitBetweenSamplingMs);
                }
                catch (InterruptedException e)
                {
                }
            }
        }
    }
    
    
    public int getTotal()
    {
        if (this.root==null)
        {
            return 0;
        }
        return this.root.getTotal();
    }
    
    public StackTraceElementNode getRoot()
    {
        return this.root;
    }
    
}
