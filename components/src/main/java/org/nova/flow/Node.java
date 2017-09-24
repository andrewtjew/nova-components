package org.nova.flow;

public abstract class Node
{
	abstract public void process(Packet packet) throws Throwable;
    abstract public void flush() throws Throwable;
    abstract public void beginGroup(long groupIdentifier) throws Throwable;
    abstract public void endGroup() throws Throwable;
}
