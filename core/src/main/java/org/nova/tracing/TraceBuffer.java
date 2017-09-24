package org.nova.tracing;


public class TraceBuffer
{
    private int readIndex;
    private int writeIndex;
    private int size;
    
    final private Trace[] array;
    final private int length;
    
    public TraceBuffer(int capacity)
    {
        this.array=new Trace[capacity];
        this.length=array.length;
    }
    public void add(Trace trace)
    {
        this.array[this.writeIndex]=trace;
        this.writeIndex=(this.writeIndex+1)%this.length;
        if (this.size<this.length)
        {
            this.size++;
        }
        else
        {
            this.readIndex=(this.readIndex+1)%this.length;
        }
    }
    public int size()
    {
        return this.size;
    }
    public Trace[] getSnapshot()
    {
        int size=size();
        Trace[] snapshot=new Trace[size];
        for (int i=0;i<size;i++)
        {
            snapshot[i]=this.array[(this.readIndex+i)%this.length];
        }
        return snapshot;
    }
}
