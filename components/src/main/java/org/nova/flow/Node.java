package org.nova.flow;

public abstract class Node
{
	abstract public void process(Packet packet) throws Throwable;
    abstract public void flush() throws Throwable;
    abstract public void beginSegment(long segmentIndex) throws Throwable;
    abstract public void endSegment() throws Throwable;
}
