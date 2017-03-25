package org.nova.tracing;


public class TraceBuffer
{
	private int readIndex;
	private int writeIndex;
	final private Trace[] array;
	final private int length;
	public TraceBuffer(int size)
	{
		this.array=new Trace[size];
		this.length=array.length;
	}
	public void add(Trace item)
	{
		this.array[(this.writeIndex+this.length)%this.length]=item;
		this.writeIndex=(this.writeIndex+this.length+1)%this.length;
	}
	public int size()
	{
		return (this.writeIndex+this.length-this.readIndex)%length;
	}
	public Trace[] getSnapshot()
	{
        int size=size();
	    Trace[] traces=new Trace[size];
		for (int i=0;i<size;i++)
		{
		    traces[i]=this.array[(this.readIndex+i)%this.length];
		}
		return traces;
	}
}
