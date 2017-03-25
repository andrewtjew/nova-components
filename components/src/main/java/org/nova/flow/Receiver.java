package org.nova.flow;

public abstract class Receiver
{
	abstract public void send(Packet container) throws Throwable;
    abstract public void flush() throws Throwable;
    abstract public void beginSegment(long marker) throws Throwable;
    abstract public void endSegment() throws Throwable;
}
